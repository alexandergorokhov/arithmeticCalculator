package repository;

import static org.challenge.util.Constants.ADDITION;

import org.challenge.ArithmeticCalculator;
import org.challenge.domain.Operation;
import org.challenge.repository.OperationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@SpringBootTest(classes = {ArithmeticCalculator.class})
@ExtendWith(SpringExtension.class)
public class OperationRepositoryTest {

    @Autowired
    private OperationRepository operationRepository;



    @Test
    public void testFindById_OperationExists_ReturnsOperation() {
        // Arrange
        Operation operation = new Operation();
        operation.setId(1L);
        operation.setType(ADDITION);


        // Act
        Optional<Operation> result = operationRepository.findById(1L);

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(operation.getId(), result.get().getId());
        Assertions.assertEquals(operation.getType(), result.get().getType());
    }

    @Test
    public void testFindById_OperationDoesNotExist_ReturnsEmptyOptional() {
        // Arrange
        // Act
        Optional<Operation> result = operationRepository.findById(10L);
        // Assert
        Assertions.assertFalse(result.isPresent());
    }
}
