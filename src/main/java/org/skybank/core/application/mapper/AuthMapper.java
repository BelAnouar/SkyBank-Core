package org.skybank.core.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.skybank.core.application.dto.response.*;
import org.skybank.core.domain.model.Account;


@Mapper(componentModel = "default")
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);


    @Mapping(target = "transactionCount", expression = "java(account.getTransactions() != null ? account.getTransactions().size() : 0)")
    AccountResponse toAccountResponse(Account account);


    @Mapping(target = "success", constant = "true")
    @Mapping(target = "message", constant = "Account created and signed in successfully")
    CreateAccountResponse toCreateAccountResponse(Account account);

    default SignInResponse toSignInResponse(Account account) {
        return new SignInResponse(
                true,
                "Signed in successfully",
                toAccountResponse(account)
        );
    }

   default CreateAccountResponse toCreateAccountErrorResponse(String errorMessage) {
        return new CreateAccountResponse(
                false,
                errorMessage,
                null,
                0
        );
    }

    default SignInResponse toSignInErrorResponse(String errorMessage) {
        return new SignInResponse(
                false,
                errorMessage,
                null
        );
    }
}