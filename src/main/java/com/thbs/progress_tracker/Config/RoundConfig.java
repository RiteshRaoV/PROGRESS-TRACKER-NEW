package com.thbs.progress_tracker.Config;

import org.springframework.stereotype.Component;

@Component
public class RoundConfig {
    public double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
