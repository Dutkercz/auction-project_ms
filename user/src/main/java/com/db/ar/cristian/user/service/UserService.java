package com.db.ar.cristian.user.service;

import com.db.ar.cristian.user.dto.UserRequest;
import com.db.ar.cristian.user.dto.UserResponse;
import com.db.ar.cristian.user.exception.UserExistsException;
import com.db.ar.cristian.user.exception.UserNotFoundException;
import com.db.ar.cristian.user.mapper.BidMapper;
import com.db.ar.cristian.user.mapper.UserMapper;
import com.db.ar.cristian.user.messaging.AuctionBidsConsumer;
import com.db.ar.cristian.user.model.BidReference;
import com.db.ar.cristian.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BidMapper bidMapper;

    @Transactional
    public UserResponse createUser(UserRequest request){
        Boolean existUser = userRepository.existsByDoc(request.doc());
        if (existUser){
            throw new UserExistsException("Usuário já cadastrado.");
        }
        var user = userMapper.toEntity(request);
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public UserResponse findById(Long id) {
        var user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("Usuário não encontrado"));
        return userMapper.toResponse(user);
    }

    @Transactional
    public void addUserBidReference(AuctionBidsConsumer.BidRepresentation bidRepresentation) {
        var user = userRepository.findById(bidRepresentation.bidderId()).orElseThrow(() ->
                             new UserNotFoundException("Usuário não encontrado"));

        BidReference reference = bidMapper.toReference(bidRepresentation);

        user.addBid(reference);
    }
}
