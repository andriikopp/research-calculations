cat("\f")
setwd("D:/GitHub/research-calculations/research-calculations-r")

library(caret)

training_data <- read.csv("pdm_telemetry_2015.csv")

logit_model <- glm(formula = failure ~ volt + rotate + pressure + vibration, 
             data = training_data, family = binomial)

print(summary(logit_model))

prediction <- predict(logit_model, newdata = training_data, type="response")

confusionMatrix(as.factor(prediction > 0.5), as.factor(training_data$failure > 0))
