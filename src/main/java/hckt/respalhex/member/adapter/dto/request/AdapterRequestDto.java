package hckt.respalhex.member.adapter.dto.request;

public interface AdapterRequestDto<T> {
    T convertToApplicationDto();
}
