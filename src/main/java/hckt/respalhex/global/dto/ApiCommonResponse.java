package hckt.respalhex.global.dto;

public record ApiCommonResponse<T>(boolean success, T result) {
}
