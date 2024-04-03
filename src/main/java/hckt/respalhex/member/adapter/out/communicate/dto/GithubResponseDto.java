package hckt.respalhex.member.adapter.out.communicate.dto;

public class GithubResponseDto extends OAuthCommunicateResponseDto{
    private String id;
    private String avatarUrl;
    private String login;

    @Override
    public void initAllFields() {
        init(super.getEmail(), this.avatarUrl, this.login);
    }
}
