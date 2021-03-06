package ca.ulaval.glo4003.evulution.api.authorization;

import ca.ulaval.glo4003.evulution.api.authorization.assemblers.TokenDtoAssembler;
import ca.ulaval.glo4003.evulution.api.mappers.assemblers.HTTPExceptionResponseAssembler;
import ca.ulaval.glo4003.evulution.service.authorization.AuthorizationService;
import ca.ulaval.glo4003.evulution.service.authorization.dto.TokenDto;
import ca.ulaval.glo4003.evulution.service.exceptions.ServiceBadInputParameterException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class SecuredWithSaleIdAuthorizationFilterTest {
    private static final String A_HEADER_STRING = "ASDF";
    private static final String A_VALID_PATH_PARAM = "1";
    private static final int A_VALID_SALE_ID = 1;
    private static final String AN_INVALID_PATH_PARAM = "invalid";

    @Mock
    ContainerRequestContext containerRequestContext;

    @Mock
    HTTPExceptionResponseAssembler httpExceptionResponseAssembler;

    @Mock
    AuthorizationService authorizationService;

    @Mock
    TokenDtoAssembler tokenDtoAssembler;

    @Mock
    TokenDto tokenDto;

    @Mock
    UriInfo uriInfo;

    SecuredWithSaleIdAuthorizationFilter securedWithSaleIdAuthorizationFilter;

    @BeforeEach
    void setUp() {
        securedWithSaleIdAuthorizationFilter = new SecuredWithSaleIdAuthorizationFilter(authorizationService,
                tokenDtoAssembler, httpExceptionResponseAssembler);
    }

    @Test
    public void whenFilter_thenContainerRequestContextGetsAuthorizationHeader() {
        // given
        BDDMockito.given(containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION))
                .willReturn(A_HEADER_STRING);
        BDDMockito.given(containerRequestContext.getUriInfo()).willReturn(uriInfo);
        BDDMockito.given(tokenDtoAssembler.assembleFromString(A_HEADER_STRING)).willReturn(tokenDto);
        BDDMockito.given(uriInfo.getPathParameters()).willReturn(new MultivaluedHashMap<String, String>() {
            {
                put("", Arrays.asList(A_VALID_PATH_PARAM));
            }
        });

        // when
        this.securedWithSaleIdAuthorizationFilter.filter(containerRequestContext);

        // then
        Mockito.verify(containerRequestContext).getHeaderString(HttpHeaders.AUTHORIZATION);
    }

    @Test
    public void whenFilter_thenTokenDtoAssemblesAssembleFromString() {
        // given
        BDDMockito.given(containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION))
                .willReturn(A_HEADER_STRING);
        BDDMockito.given(containerRequestContext.getUriInfo()).willReturn(uriInfo);
        BDDMockito.given(tokenDtoAssembler.assembleFromString(A_HEADER_STRING)).willReturn(tokenDto);
        BDDMockito.given(uriInfo.getPathParameters()).willReturn(new MultivaluedHashMap<String, String>() {
            {
                put("", Arrays.asList(A_VALID_PATH_PARAM));
            }
        });

        // when
        this.securedWithSaleIdAuthorizationFilter.filter(containerRequestContext);

        // then
        Mockito.verify(tokenDtoAssembler).assembleFromString(A_HEADER_STRING);
    }

    @Test
    public void whenFilter_thenAuthorizationServiceIsCalled() {
        BDDMockito.given(containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION))
                .willReturn(A_HEADER_STRING);
        BDDMockito.given(containerRequestContext.getUriInfo()).willReturn(uriInfo);
        BDDMockito.given(tokenDtoAssembler.assembleFromString(A_HEADER_STRING)).willReturn(tokenDto);
        BDDMockito.given(uriInfo.getPathParameters()).willReturn(new MultivaluedHashMap<String, String>() {
            {
                put("", Arrays.asList(A_VALID_PATH_PARAM));
            }
        });

        // when
        securedWithSaleIdAuthorizationFilter.filter(containerRequestContext);

        // then
        BDDMockito.verify(authorizationService).validateTokenWithSaleId(tokenDto, A_VALID_SALE_ID);
    }

    @Test
    public void givenInvalidPathParam_whenFilter_thenAssembleException() {
        BDDMockito.given(containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION))
                .willReturn(A_HEADER_STRING);
        BDDMockito.given(containerRequestContext.getUriInfo()).willReturn(uriInfo);
        BDDMockito.given(tokenDtoAssembler.assembleFromString(A_HEADER_STRING)).willReturn(tokenDto);
        BDDMockito.given(uriInfo.getPathParameters()).willReturn(new MultivaluedHashMap<String, String>() {
            {
                put("", Arrays.asList(AN_INVALID_PATH_PARAM));
            }
        });

        // when
        securedWithSaleIdAuthorizationFilter.filter(containerRequestContext);

        // then
        BDDMockito.verify(httpExceptionResponseAssembler)
                .assembleResponseFromExceptionClass(ServiceBadInputParameterException.class);
    }

}
