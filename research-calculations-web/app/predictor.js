var predictor = {
    movingAverage: function(timeseries) {
        var movingAverageTimeSeries = [];
        var sumOfErrors = 0;

        for (var i = 1; i < timeseries.length - 1; i++) {
            movingAverageTimeSeries[i] = (timeseries[i - 1] + timeseries[i] + timeseries[i + 1]) / 3;
            sumOfErrors += Math.abs((timeseries[i] - movingAverageTimeSeries[i]) / timeseries[i]);
        }

        return {
            prediction: movingAverageTimeSeries[timeseries.length - 2] + (1 / 3) *
                (timeseries[timeseries.length - 1] - timeseries[timeseries.length - 2]),
            error: (1 / (timeseries.length - 2)) * sumOfErrors
        };
    },

    exponentialSmoothing: function(timeseries) {
        var alpha = 2 / (timeseries.length + 1);
        var sumOfErrors = 0;

        var sumOfTimeseriesValues = 0;

        for (var i = 0; i < timeseries.length; i++) {
            sumOfTimeseriesValues += timeseries[i];
        }

        var smoothedTimeseries = [sumOfTimeseriesValues / timeseries.length];

        for (var i = 1; i < timeseries.length; i++) {
            smoothedTimeseries[i] = timeseries[i - 1] * alpha + (1 - alpha) * smoothedTimeseries[i - 1];
        }

        for (var i = 0; i < timeseries.length; i++) {
            sumOfErrors += Math.abs((timeseries[i] - smoothedTimeseries[i]) / timeseries[i]);
        }

        return {
            prediction: timeseries[timeseries.length - 1] * alpha + (1 - alpha) *
                smoothedTimeseries[smoothedTimeseries.length - 1],
            error: (1 / timeseries.length) * sumOfErrors
        };
    },

    linearRegression: function(timeseries) {
        var sumXY = 0;

        for (var i = 0; i < timeseries.length; i++) {
            sumXY += timeseries[i] * (i + 1);
        }

        var sumX = 0;

        for (var i = 0; i < timeseries.length; i++) {
            sumX += (i + 1);
        }

        var sumY = 0;

        for (var i = 0; i < timeseries.length; i++) {
            sumY += timeseries[i];
        }

        var sumXX = 0;

        for (var i = 0; i < timeseries.length; i++) {
            sumXX += (i + 1) * (i + 1);
        }

        var a = (sumXY - ((sumX * sumY) / timeseries.length)) /
            (sumXX - ((sumX * sumX) / timeseries.length));
        var b = (sumY / timeseries.length) - ((a * sumX) / timeseries.length);

        var sumOfErrors = 0;

        for (var i = 0; i < timeseries.length; i++) {
            sumOfErrors += Math.abs((timeseries[i] - (a * (i + 1) + b)) / timeseries[i]);
        }

        return {
            prediction: a * (timeseries.length + 1) + b,
            error: (1 / timeseries.length) * sumOfErrors
        };
    }
};

var predictorTest = {
    test: function() {
        var testData = [8599.76, 8563.26, 8865.39, 8788.54, 8760.29, 9078.31,
            9121.60, 8908.21, 8111.15, 7922.15
        ];

        console.log(predictor.movingAverage(testData));
        console.log(predictor.exponentialSmoothing(testData));
        console.log(predictor.linearRegression(testData));
    }
};

predictorTest.test();
