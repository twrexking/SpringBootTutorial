package com.vincent.demo.service;

import com.vincent.demo.model.PostPO;
import com.vincent.demo.model.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {
    private final Map<String, PostPO> postDB = new HashMap<>();

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    public PostPO createPost(String id, String userId, String title) {
        var post = PostPO.of(id, userId, title);
        postDB.put(post.getId(), post);

        return post;
    }

    public PostVO getPost(String id) {
        var postPO = postDB.get(id);
        var creatorName = userService.getUser(postPO.getCreatorId()).getName();
        var likeUserNames = likeService.getLikeUserNames(id);

        return PostVO.of(postPO, creatorName, likeUserNames);
    }

    public List<PostVO> getMostLikedPosts() {
        var postToLikeCountMap = new HashMap<String, Long>();
        postDB.values()
                .forEach(post -> {
                    var likeCount = likeService.getLikeCount(post.getId());
                    postToLikeCountMap.put(post.getId(), likeCount);
                });

        var posts = postToLikeCountMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .map(x -> getPost(x.getKey()))
                .toList();
        return new ArrayList<>(posts);
    }

    public void deleteAll() {
        postDB.clear();
    }
}
