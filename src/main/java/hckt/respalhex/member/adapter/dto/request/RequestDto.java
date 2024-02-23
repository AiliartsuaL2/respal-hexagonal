package hckt.respalhex.member.adapter.dto.request;

public interface RequestDto<T> {
    T convertToApplicationDto();
}
