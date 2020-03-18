setwd("D:/GitHub/research-calculations/research-calculations-r")

training <- read.csv("pdm_telemetry_2015.csv")

training_ts <- c(training$volt)
training_ts <- training_ts[training_ts != 0]

# moving average
ma_ts <- c()
sum_err <- 0

for ( i in 1:length( training_ts ) ) {
  if ( ( i > 1 ) & (i < length( training_ts ) ) ) {
    ma_ts[i] <- ( training_ts[i - 1] + training_ts[i] + training_ts[i + 1] ) / 3
    sum_err <- sum_err + ( abs( ( training_ts[i] - ma_ts[i] ) / training_ts[i] ) * 100 )
  }
}

pred <- ma_ts[length( training_ts ) - 1] + ( 1 / 3 ) * 
  ( training_ts[length( training_ts )] - training_ts[length( training_ts ) - 1] )
pred

err <- ( 1 / ( length( training_ts ) - 2 ) ) * sum_err
err

# exponential smoothing
alpha <- 2 / ( length( training_ts ) + 1 )
sum_err <- 0

es_ts <- c(mean( training_ts ))

for ( i in 2:length( training_ts ) ) {
  es_ts[i] <- training_ts[i - 1] * alpha + ( 1 - alpha ) * es_ts[i - 1]
}

for ( i in 1:length( training_ts ) ) {
  sum_err <- sum_err + ( abs( ( training_ts[i] - es_ts[i] ) / training_ts[i] ) * 100 )
}

pred <- training_ts[length( training_ts )] * alpha + 
  ( 1 - alpha ) * es_ts[length( es_ts )]
pred

err <- ( 1 / ( length( training_ts ) ) ) * sum_err
err

# regression
sum_xy <- 0

for ( i in 1:length( training_ts ) ) {
  sum_xy <- sum_xy + ( training_ts[i] * i )
}

sum_x <- 0

for ( i in 1:length( training_ts ) ) {
  sum_x <- sum_x + i
}

sum_y <- 0

for ( i in 1:length( training_ts ) ) {
  sum_y <- sum_y + training_ts[i]
}

sum_x2 <- 0

for ( i in 1:length( training_ts ) ) {
  sum_x2 <- sum_x2 + ( i * i )
}

a <- ( sum_xy - ( sum_x * sum_y ) / length( training_ts ) ) / 
  ( sum_x2 - ( sum_x ) / length( training_ts ) )

b <- ( sum_y / length( training_ts ) ) - a * ( sum_x / length( training_ts ) )

pred <- a * ( length( training_ts ) + 1 ) + b
pred

sum_err <- 0

for ( i in 1:length( training_ts ) ) {
  regr <- a * i + b
  sum_err <- sum_err + ( abs( ( training_ts[i] - regr ) / training_ts[i] ) * 100 )
}

err <- ( 1 / ( length( training_ts ) ) ) * sum_err
err