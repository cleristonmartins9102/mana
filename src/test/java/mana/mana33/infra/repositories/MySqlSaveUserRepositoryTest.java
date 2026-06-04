package mana.mana33.infra.repositories;

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
class MySqlSaveUserRepositoryTest {

    @Mock
    private JpaAccountRepository jpaAccountRepository;

    @InjectMocks
    private MySqlSaveUserRepository mySqlSaveUserRepository;

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
        mySqlSaveUserRepository.save(saveUserModel);

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
        mySqlSaveUserRepository.save(saveUserModel);

        verify(jpaAccountRepository, times(1)).save(any(AccountEntity.class));
    }
}
