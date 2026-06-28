package com.phtransitgraph.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.phtransitgraph.dto.request.OperatorRequest;
import com.phtransitgraph.dto.response.OperatorResponse;
import com.phtransitgraph.entity.Operator;
import com.phtransitgraph.exception.DuplicateResourceException;
import com.phtransitgraph.exception.ResourceNotFoundException;
import com.phtransitgraph.repository.OperatorRepository;
import com.phtransitgraph.security.OwnershipValidator;

@Service
public class OperatorService {

    private final OperatorRepository operatorRepository;
    private final OwnershipValidator ownershipValidator;

    public OperatorService(OperatorRepository operatorRepository,
            OwnershipValidator ownershipValidator) {
        this.operatorRepository = operatorRepository;
        this.ownershipValidator = ownershipValidator;
    }

    private OperatorResponse toResponse(Operator operator) {
        return new OperatorResponse(
                operator.getId(),
                operator.getUser().getId(),
                operator.getUser().getFullName(),
                operator.getUser().getEmail(),
                operator.getOperatorName(),
                operator.getFranchiseNo(),
                operator.getContactNumber(),
                operator.isVerified(),
                operator.getCreatedAt());
    }

    public List<OperatorResponse> getAllVerifiedOperators() {
        return operatorRepository.findByVerifiedTrue()
                .stream().map(this::toResponse).toList();
    }

    public List<OperatorResponse> getPendingOperators() {
        return operatorRepository.findByVerifiedFalse()
                .stream().map(this::toResponse).toList();
    }

    public OperatorResponse getOperatorById(String id) {
        Operator operator = operatorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Operator not found with id: " + id));
        return toResponse(operator);
    }

    public OperatorResponse updateProfile(String email, OperatorRequest req) {
        Operator operator = ownershipValidator.getOperatorFromEmail(email);
        if (req.getFranchiseNo() != null &&
                !req.getFranchiseNo().equals(operator.getFranchiseNo()) &&
                operatorRepository.existsByFranchiseNo(req.getFranchiseNo())) {
            throw new DuplicateResourceException(
                    "Franchise number '" + req.getFranchiseNo() + "' is already in use");
        }
        operator.setOperatorName(req.getOperatorName());
        operator.setFranchiseNo(req.getFranchiseNo());
        operator.setContactNumber(req.getContactNumber());
        return toResponse(operatorRepository.save(operator));
    }

    public OperatorResponse verifyOperator(String id) {
        Operator operator = operatorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Operator not found with id: " + id));
        operator.setVerified(true);
        return toResponse(operatorRepository.save(operator));
    }
}
