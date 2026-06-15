import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';
import papaparse from 'https://jslib.k6.io/papaparse/5.1.1/index.js';

// Configuration from environment variables or defaults
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const DATA_FILE = __ENV.DATA_FILE || '../data/product.csv';
const SCENARIO_TYPE = __ENV.SCENARIO || 'baseline';

// Load CSV data
const csvData = new SharedArray('Products', function () {
    return papaparse.parse(open(DATA_FILE), { header: true }).data;
});

// Define options based on scenario
const scenarios = {
    baseline: {
        executor: 'ramping-vus',
        startVUs: 0,
        stages: [
            { duration: '1m', target: 50 }, // Warmup and baseline
            { duration: '3m', target: 50 }, // Sustained load
            { duration: '1m', target: 0 },  // Ramp down
        ],
    },
    load: {
        executor: 'ramping-vus',
        startVUs: 0,
        stages: [
            { duration: '5m', target: 200 }, // Ramp up to 200 VUs
            { duration: '10m', target: 200 }, // Sustained load
            { duration: '5m', target: 0 },    // Ramp down
        ],
    },
    stress: {
        executor: 'ramping-vus',
        startVUs: 0,
        stages: [
            { duration: '5m', target: 200 },  // Normal load
            { duration: '10m', target: 600 }, // Stress
            { duration: '5m', target: 0 },    // Ramp down
        ],
    },
    spike: {
        executor: 'ramping-vus',
        startVUs: 0,
        stages: [
            { duration: '1m', target: 50 },   // Normal load
            { duration: '1m', target: 800 },  // Spike!
            { duration: '3m', target: 800 },  // Hold Spike
            { duration: '1m', target: 50 },   // Normal load
            { duration: '2m', target: 0 },    // Ramp down
        ],
    },
    soak: {
        executor: 'ramping-vus',
        startVUs: 0,
        stages: [
            { duration: '5m', target: 200 },  // Ramp up
            { duration: '1h', target: 200 },  // Hold for 1 hour
            { duration: '5m', target: 0 },    // Ramp down
        ],
    }
};

export const options = {
    scenarios: {
        execution: scenarios[SCENARIO_TYPE],
    },
    thresholds: {
        // SLA / SLO definitions based on the rubric
        http_req_duration: ['p(95)<=300', 'p(99)<=800'], // Latencies
        http_req_failed: ['rate<0.01'],                 // Error rate < 1%
    },
};

export default function () {
    // Pick a random product from the CSV
    const product = csvData[Math.floor(Math.random() * csvData.length)];

    const payload = JSON.stringify({
        name: product.name,
        description: product.description,
        price: parseFloat(product.price),
        stock: parseInt(product.stock),
        categoryId: parseInt(product.categoryId),
        locationId: parseInt(product.locationId)
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    // Make the POST request
    const res = http.post(`${BASE_URL}/api/products`, payload, params);

    // Asserts (Checks)
    check(res, {
        'status is 201': (r) => r.status === 201, // Based on ProductController, @PostMapping returns HttpStatus.CREATED
        'response has id': (r) => r.json('id') !== undefined,
    });

    // Short sleep between iterations to simulate real user thinking time
    sleep(1);
}
