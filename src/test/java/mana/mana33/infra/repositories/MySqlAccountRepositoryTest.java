package mana.mana33.infra.repositories;

import mana.mana33.domain.models.AccountModel;
import mana.mana33.domain.models.SaveUserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MySqlAccountRepositoryTest {

    @Mock
    private JpaAccountRepository jpaAccountRepository;

    @InjectMocks
    private MySqlAccountRepository mySqlAccountRepository;

    private SaveUserModel saveUserModel;

    @BeforeEach
    void setUp() {
        saveUserModel = new SaveUserModel(
                "John",
                "Doe",
                "john.doe@example.com",
                "password123",
                "+1234567890",
                "refresh-token-xyz"
        );
    }

    @Test
    void shouldSaveUserModelAsAccountEntity() {
        AccountEntity savedEntity = createSavedEntity();
        when(jpaAccountRepository.save(any(AccountEntity.class))).thenReturn(savedEntity);

        mySqlAccountRepository.save(saveUserModel);

        ArgumentCaptor<AccountEntity> entityCaptor = ArgumentCaptor.forClass(AccountEntity.class);
        verify(jpaAccountRepository, times(1)).save(entityCaptor.capture());

        AccountEntity capturedEntity = entityCaptor.getValue();
        assertNotNull(capturedEntity);
        assertEquals("John", capturedEntity.getFirstName());
        assertEquals("Doe", capturedEntity.getSecondName());
        assertEquals("john.doe@example.com", capturedEntity.getEmail());
        assertEquals("password123", capturedEntity.getPassword());
        assertEquals("+1234567890", capturedEntity.getMobileNumber());
        assertEquals("refresh-token-xyz", capturedEntity.getRefreshToken());
    }

    @Test
    void shouldCallJpaRepositorySaveOnce() {
        AccountEntity savedEntity = createSavedEntity();
        when(jpaAccountRepository.save(any(AccountEntity.class))).thenReturn(savedEntity);

        mySqlAccountRepository.save(saveUserModel);

        verify(jpaAccountRepository, times(1)).save(any(AccountEntity.class));
    }

    @Test
    void shouldMapAllFieldsFromModelToEntity() {
        AccountEntity savedEntity = createSavedEntity();
        when(jpaAccountRepository.save(any(AccountEntity.class))).thenReturn(savedEntity);

        mySqlAccountRepository.save(saveUserModel);

        ArgumentCaptor<AccountEntity> entityCaptor = ArgumentCaptor.forClass(AccountEntity.class);
        verify(jpaAccountRepository).save(entityCaptor.capture());

        AccountEntity entity = entityCaptor.getValue();
        assertEquals(saveUserModel.firstName(), entity.getFirstName());
        assertEquals(saveUserModel.secondName(), entity.getSecondName());
        assertEquals(saveUserModel.email(), entity.getEmail());
        assertEquals(saveUserModel.password(), entity.getPassword());
        assertEquals(saveUserModel.mobileNumber(), entity.getMobileNumber());
        assertEquals(saveUserModel.refreshToken(), entity.getRefreshToken());
    }

    @Test
    void shouldHandleNullRefreshToken() {
        SaveUserModel modelWithNullToken = new SaveUserModel(
                "Jane",
                "Smith",
                "jane.smith@example.com",
                "password456",
                "+9876543210",
                null
        );

        AccountEntity savedEntity = createSavedEntity();
        when(jpaAccountRepository.save(any(AccountEntity.class))).thenReturn(savedEntity);

        mySqlAccountRepository.save(modelWithNullToken);

        ArgumentCaptor<AccountEntity> entityCaptor = ArgumentCaptor.forClass(AccountEntity.class);
        verify(jpaAccountRepository).save(entityCaptor.capture());

        AccountEntity entity = entityCaptor.getValue();
        assertNull(entity.getRefreshToken());
    }

    @Test
    void shouldCreateNewEntityForEachSave() {
        SaveUserModel model1 = new SaveUserModel("User1", "Last1", "user1@example.com", "pass1", "111", "token1");
        SaveUserModel model2 = new SaveUserModel("User2", "Last2", "user2@example.com", "pass2", "222", "token2");

        AccountEntity savedEntity = createSavedEntity();
        when(jpaAccountRepository.save(any(AccountEntity.class))).thenReturn(savedEntity);

        mySqlAccountRepository.save(model1);
        mySqlAccountRepository.save(model2);

        verify(jpaAccountRepository, times(2)).save(any(AccountEntity.class));
    }

    @Test
    void shouldReturnAccountModelAfterSave() {
        AccountEntity savedEntity = createSavedEntity();
        when(jpaAccountRepository.save(any(AccountEntity.class))).thenReturn(savedEntity);

        AccountModel result = mySqlAccountRepository.save(saveUserModel);

        assertNotNull(result);
        assertEquals("123", result.id());
        assertEquals("John", result.firstName());
        assertEquals("Doe", result.secondName());
        assertEquals("john.doe@example.com", result.email());
        assertEquals("+1234567890", result.mobileNumber());
        assertNull(result.token());
        assertEquals("refresh-token-xyz", result.refreshToken());
    }

    private AccountEntity createSavedEntity() {
        AccountEntity entity = new AccountEntity();
        entity.setId(123L);
        entity.setFirstName("John");
        entity.setSecondName("Doe");
        entity.setEmail("john.doe@example.com");
        entity.setPassword("password123");
        entity.setMobileNumber("+1234567890");
        entity.setRefreshToken("refresh-token-xyz");
        return entity;
    }
}
