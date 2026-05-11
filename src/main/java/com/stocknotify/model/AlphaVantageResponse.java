package com.stocknotify.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AlphaVantageResponse {
    @JsonProperty("Global Quote")
    private GlobalQuote globalQuote;
    @JsonProperty("Error Message")
    private String errorMessage;
    @JsonProperty("Note")
    private String note;
    @JsonProperty("Information")
    private String information;

    @Data
    public static class GlobalQuote {
        @JsonProperty("01. symbol")
        private String symbol;
        @JsonProperty("02. open")
        private String open;
        @JsonProperty("03. high")
        private String high;
        @JsonProperty("04. low")
        private String low;
        @JsonProperty("05. price")
        private String price;
        @JsonProperty("06. volume")
        private String volume;
    }
}
