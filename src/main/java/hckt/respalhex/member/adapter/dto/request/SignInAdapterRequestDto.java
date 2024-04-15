package hckt.respalhex.member.adapter.dto.request;


import hckt.respalhex.member.application.dto.request.SignInMemberRequestDto;

public record SignInAdapterRequestDto(String email, String password, String client) implements AdapterRequestDto<SignInMemberRequestDto> {

    @Override
    public SignInMemberRequestDto convertToApplicationDto() {
        return new SignInMemberRequestDto(email, password);
    }
}
