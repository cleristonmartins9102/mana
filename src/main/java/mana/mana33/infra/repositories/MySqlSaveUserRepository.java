package mana.mana33.infra.repositories;

import mana.mana33.domain.SaveUserRepository;
import mana.mana33.domain.models.AccountModel;
import mana.mana33.domain.models.SaveUserModel;
import org.springframework.stereotype.Component;

@Component
public class MySqlSaveUserRepository implements SaveUserRepository {

    private final JpaAccountRepository jpaAccountRepository;

    public MySqlSaveUserRepository(JpaAccountRepository jpaAccountRepository) {
        this.jpaAccountRepository = jpaAccountRepository;
    }

    @Override
    public AccountModel save(SaveUserModel model) {
        AccountEntity entity = new AccountEntity();
        entity.setFirstName(model.firstName());
        entity.setSecondName(model.secondName());
        entity.setEmail(model.email());
        entity.setPassword(model.password());
        entity.setMobileNumber(model.mobileNumber());
        entity.setRefreshToken(model.refreshToken());

        AccountEntity savedEntity = jpaAccountRepository.save(entity);

        return new AccountModel(
            savedEntity.getId().toString(),
            savedEntity.getFirstName(),
            savedEntity.getSecondName(),
            savedEntity.getEmail(),
            savedEntity.getMobileNumber()
        );
    }
}
