from numpy import exp, array, random, dot

training_set_inputs = array([
    # Function, Event, Gateway, In = 0, In = 1, In > 1, Out = 0, Out = 1, Out > 1
    [1, 0, 0, 1, 0, 0, 1, 0, 0],
    [1, 0, 0, 1, 0, 0, 0, 1, 0],
    [1, 0, 0, 1, 0, 0, 0, 0, 1],
    [1, 0, 0, 0, 1, 0, 1, 0, 0],
    [1, 0, 0, 0, 1, 0, 0, 1, 0],
    [1, 0, 0, 0, 1, 0, 0, 0, 1],
    [1, 0, 0, 0, 0, 1, 1, 0, 0],
    [1, 0, 0, 0, 0, 1, 0, 1, 0],
    [1, 0, 0, 0, 0, 1, 0, 0, 1],
    [0, 1, 0, 1, 0, 0, 1, 0, 0],
    [0, 1, 0, 1, 0, 0, 0, 1, 0],
    [0, 1, 0, 1, 0, 0, 0, 0, 1],
    [0, 1, 0, 0, 1, 0, 1, 0, 0],
    [0, 1, 0, 0, 1, 0, 0, 1, 0],
    [0, 1, 0, 0, 1, 0, 0, 0, 1],
    [0, 1, 0, 0, 0, 1, 1, 0, 0],
    [0, 1, 0, 0, 0, 1, 0, 1, 0],
    [0, 1, 0, 0, 0, 1, 0, 0, 1],
    [0, 0, 1, 1, 0, 0, 1, 0, 0],
    [0, 0, 1, 1, 0, 0, 0, 1, 0],
    [0, 0, 1, 1, 0, 0, 0, 0, 1],
    [0, 0, 1, 0, 1, 0, 1, 0, 0],
    [0, 0, 1, 0, 1, 0, 0, 1, 0],
    [0, 0, 1, 0, 1, 0, 0, 0, 1],
    [0, 0, 1, 0, 0, 1, 1, 0, 0],
    [0, 0, 1, 0, 0, 1, 0, 1, 0],
    [0, 0, 1, 0, 0, 1, 0, 0, 1]
])

training_set_outputs = array([
    [0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0]
]).T

random.seed(1)

synaptic_weights = 2 * random.random((9, 1)) - 1

for iteration in range(10000):
    output = 1 / (1 + exp(-(dot(training_set_inputs, synaptic_weights))))
    synaptic_weights += dot(training_set_inputs.T, (training_set_outputs - output) * output * (1 - output))

for test_case in training_set_inputs:
    print(1 / (1 + exp(-(dot(array(test_case), synaptic_weights)))))

print(synaptic_weights)
