package ca.ulaval.glo4003.evulution.api.authorization.dto;

public class TokenDto {
    private final String token;

    public TokenDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
