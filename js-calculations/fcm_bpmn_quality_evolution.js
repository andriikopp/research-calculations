let corrMatrix = [
    [1.0000000000, -0.012354979, 0.04217199, 0.0002040057, 0.018768495],
    [-0.0123549793, 1.000000000, 0.34532811, 0.3738154969, -0.006134204],
    [0.0421719911, 0.345328111, 1.00000000, 0.3959047787, -0.033807444],
    [0.0002040057, 0.373815497, 0.39590478, 1.0000000000, -0.003051179],
    [0.0187684955, -0.006134204, -0.03380744, -0.0030511788, 1.000000000]
];

let modelVector = [1, 1, 1, 1, 1];

console.log(0, modelVector);

let n = modelVector.length;

let RUN_TIMES = 100;
let eps = 10e-6;

let f = (x) => 1 / (1 + Math.exp(-x));

for (let t = 1;; t++) {
    let newModelVector = [];

    for (let i = 0; i < n; i++) {
        let newVal = 0;

        for (let j = 0; j < n; j++) {
            if (j != i) {
                newVal += corrMatrix[j][i] * modelVector[j];
            }
        }

        newModelVector[i] = parseFloat(f(newVal).toFixed(4));
    }

    let stop = true;

    for (let i = 0; i < n; i++) {
        if (Math.abs(modelVector[i] - newModelVector[i]) > eps) {
            stop = false;
            break;
        }
    }

    if (stop) {
        break;
    }

    modelVector = newModelVector;

    console.log(t, modelVector);
}