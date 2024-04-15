package hckt.respalhex.global.dto;

public interface AdapterRequestDto<T> {
    T convertToApplicationDto();
}
