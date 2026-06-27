package com.phtransitgraph.security;

import com.phtransitgraph.entity.Operator;
import com.phtransitgraph.entity.Route;
import com.phtransitgraph.entity.User;
import com.phtransitgraph.exception.ResourceNotFoundException;
import com.phtransitgraph.repository.OperatorRepository;
import com.phtransitgraph.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class OwnershipValidator {
    private final UserRepository userRepository;
    private final OperatorRepository operatorRepository;

    public OwnershipValidator(UserRepository userRepository, OperatorRepository operatorRepository) {
        this.userRepository = userRepository;
        this.operatorRepository = operatorRepository;
    }

    public Operator getOperatorFromEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return operatorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Operator profile not found for this user"));
    }

    public User getUserFromEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void assertOwnsRoute(Route route, String email) {
        Operator operator = getOperatorFromEmail(email);
        if (route.getOperator() == null ||
                !route.getOperator().getId().equals(operator.getId())) {
            throw new AccessDeniedException("You do not own this route");
        }
    }
}
