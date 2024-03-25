package hckt.respalhex.global.dto;

public interface RequestDto<T> {
    T convertToApplicationDto();
}
