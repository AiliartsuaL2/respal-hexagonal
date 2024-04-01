package hckt.respalhex.member.adapter.out.communicate.dto;

public class GithubResponseDto extends OAuthCommunicateResponseDto{
    private String id;
    private String avatar_url;
    private String login;

    @Override
    public void initAllFields() {
        init(super.getEmail(), this.avatar_url, this.login);
    }
}
