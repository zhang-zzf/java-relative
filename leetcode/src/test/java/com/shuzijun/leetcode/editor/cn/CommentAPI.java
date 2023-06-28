package com.shuzijun.leetcode.editor.cn;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

public interface CommentAPI {

  /**
   * 用户商品评分
   *
   * @param userId 用户ID
   * @param itemId 商品ID
   * @param score  评分（1-5）
   */
  void score(@NotNull Long userId, @NotNull Long itemId, @NotNull Integer score);

  /**
   * 用户图文评论
   *
   * @param userId     用户ID
   * @param itemId     商品ID
   * @param commentDTO 评论
   */
  void txtComment(@NotNull Long userId, @NotNull Long itemId, @NotNull CommentDTO commentDTO);

  /**
   * 商品平均评分
   *
   * @param itemId 商品ID
   * @return 商品的平均评分
   */
  int avgScore(@NotNull Long itemId);

  /**
   * 商品评论列表
   */
  List<CommentDTO> query(@NotNull Long itemId);

  @Data
  class CommentDTO {

    private Long itemId;
    private int score;
    private String txtComment;
    private List<String> picUrlList;

  }

}
