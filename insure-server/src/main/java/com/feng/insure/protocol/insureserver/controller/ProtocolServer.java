package com.feng.insure.protocol.insureserver.controller;

import com.alibaba.fastjson.JSON;
import com.feng.insure.protocol.insureserver.controller.model.Claim;
import com.feng.insure.protocol.insureserver.controller.model.InsuranceContract;
import com.feng.insure.protocol.insureserver.controller.model.InsureOrder;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/12/6
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class ProtocolServer {

  public static final String NOT_EXIST = "NotExist";

  /**
   * 查询理赔单
   */
  @GetMapping("/claims/{reportNo}")
  public Claim queryClaimByReportNo(@PathVariable("reportNo") String reportNo) {
    log.info("/claims/{reportNo}: {}", reportNo);
    return queryClaim(reportNo);
  }

  private Claim queryClaim(String reportNo) {
    if (NOT_EXIST.equals(reportNo)) {
      return null;
    }
    String json = "{\"claimNo\":\"5de6f4f2-f795-4a50-b00d-e6a51769db73\",\"reporter\":{\"certName\":\"张碧涛\",\"mobile\":\"17779749200\"},\"accident\":{\"happenedAt\":\"2021-12-08 13:11:00\",\"province\":\"广东\",\"city\":\"深圳\",\"district\":\"南山区\",\"address\":\"南山区南新路与桂庙路交叉口向南西街92号\",\"detail\":\"2021年12月8号13时11分，申丽影驾驶无车牌电动自行车沿着桂庙路通道相向行驶，两车相撞，两车部分损坏，去了医院，我额外承担医院治疗费用，其中药费203，隔天换药费用800。\",\"data\":{\"accidentCauseLevel2\":\"DROP_ACCIDENT\",\"reportLossSum\":\"1003\",\"lossTypeList\":\"[\\\"INJURIES\\\",\\\"MATERIAL_LOSS\\\"]\",\"accidentCauseLevel1\":\"ACCIDENT_DAMAGE\"}},\"insureOrder\":{\"insureNo\":\"2021120312482850421中文knightId_1111\",\"insuranceContract\":{\"policyNo\":\"b1b96733-19e9-449c-af01-87b98ff0c0bd\"}},\"carrierReportNo\":\"reportNo: d030007a-8b5e-4b56-97a8-6f7821ad2390\",\"createdAt\":\"2021-12-10 15:32:19\",\"transactionList\":[{\"phaseId\":\"REPORT_ACCIDENT\",\"attachmentList\":[{\"type\":\"ACCIDENT\",\"fileList\":[{\"name\":\"8c7556fe-0f0a-4f0c-92aa-8c1e0bf16d52.jpg\",\"ossFileUrl\":\"\"}]},{\"type\":\"ORDER_DETAIL\",\"fileList\":[{\"name\":\"279dbddd-3169-4ec7-a848-ea8dd0e0df04.jpg\",\"ossFileUrl\":\"\"}]}],\"createdAt\":\"2021-12-10 15:32:19\"},{\"phaseId\":\"APPLY_CLAIM\",\"attachmentList\":[{\"type\":\"OTHERS\",\"fileList\":[{\"name\":\"8c7556fe-0f0a-4f0c-92aa-8c1e0bf16d52.jpg\",\"ossFileUrl\":\"\"}]}],\"data\":{\"thirdInjuredList\":\"[{\\\"firstHospitalName\\\":\\\"合肥市瑶海区第二人民医院\\\",\\\"idNo\\\":\\\"342625199606013115\\\",\\\"materialLossMaintainUnit\\\":\\\"暂时没修\\\",\\\"phone\\\":\\\"19810989182\\\"}]\",\"hospitalName\":\"合肥市瑶海区第二人民医院\"},\"createdAt\":\"2021-12-11 15:32:19\"},{\"phaseId\":\"CLAIM_NEED_MORE_DATA\",\"data\":{\"reason\":\"理赔被保司拒绝的理由：照片不清晰等\"},\"createdAt\":\"2021-12-11 15:42:19\"},{\"phaseId\":\"APPLY_CLAIM\",\"attachmentList\":[{\"type\":\"OTHERS\",\"fileList\":[{\"name\":\"\"}]}],\"data\":{\"thirdInjuredList\":\"[{\\\"firstHospitalName\\\":\\\"合肥市瑶海区第二人民医院\\\",\\\"idNo\\\":\\\"342625199606013115\\\",\\\"materialLossMaintainUnit\\\":\\\"暂时没修\\\",\\\"phone\\\":\\\"19810989182\\\"}]\",\"hospitalName\":\"合肥市瑶海区第二人民医院\"},\"createdAt\":\"2021-12-11 15:45:19\"},{\"phaseId\":\"CLAIM_ACCEPTED\",\"createdAt\":\"2021-12-11 15:50:19\"},{\"phaseId\":\"CLAIM_FINISHED\",\"createdAt\":\"2022-12-11 15:50:19\"}]}";
    return JSON.parseObject(json, Claim.class);
  }

  /**
   * 查询保单
   */
  @GetMapping("/contracts/{policyNo}")
  public List<InsureOrder> queryOrderByPolicyNo(@PathVariable("policyNo") String policyNo) {
    log.info("/contracts/{policyNo}: {}", policyNo);
    if (NOT_EXIST.equals(policyNo)) {
      throw new GlobalControllerAdvice.NotFoundException();
    }
    return queryInsureOrderByPolicyNo(policyNo);
  }

  private List<InsureOrder> queryInsureOrderByPolicyNo(String policyNo) {
    // just for test
    return queryInsureOrderByInsureNo(policyNo);
  }

  /**
   * 根据合作方的id搜索保单
   */
  @GetMapping("/contracts/_search")
  public List<InsureOrder> queryOrderByInsureNo(@RequestParam String insureNo) {
    log.info("/contracts/_search: {}", insureNo);
    if (NOT_EXIST.equals(insureNo)) {
      throw new GlobalControllerAdvice.NotFoundException();
    }
    return queryInsureOrderByInsureNo(insureNo);
  }

  private List<InsureOrder> queryInsureOrderByInsureNo(String insureNo) {
    if (NOT_EXIST.equals(insureNo)) {
      return Collections.emptyList();
    }
    String json = "{\"createdAt\":\"2021-12-03T12:48:28\",\"force\":false,\"insuranceContract\":{\"createdAt\":\"2021-12-08T16:39:10\",\"insureNo\":\"2021120312482850421中文knightId_1111\",\"policyNo\":\"aab37c6e-6ee8-41c3-90e2-7f956eabdfd2\",\"attachmentList\":[{\"createdAt\":\"2021-12-08T16:55:54.879\",\"fileList\":[{\"name\":\"digitContract_policyNo_1111.pdf\",\"ossFileUrl\":\"http://ossFileUrl\"}],\"type\":\"DIGIT_CONTRACT\"}]},\"insurant\":[{\"bizId\":\"knightId_1111\",\"certName\":\"张*峰\",\"certNo\":\"41272119******5813\",\"certType\":\"01\",\"mobile\":\"156****6513\"}],\"insureNo\":\"2021120312482850421中文knightId_1111\",\"insureProd\":{\"insuranceCarrierProd\":{\"prodCode\":\"PAYW0001\"}},\"insured\":[{\"bizId\":\"agencyId_20162244\",\"certName\":\"福建省睿谷产业园有限公司\",\"certNo\":\"91350121MA33FNRL3B\",\"certType\":\"1003\"}],\"period\":{\"end\":\"2021-12-03T23:59:59\",\"start\":\"2021-12-03T12:48:28\"},\"policyNo\":\"aab37c6e-6ee8-41c3-90e2-7f956eabdfd2\"}";
    return Arrays.asList(JSON.parseObject(json, InsureOrder.class));
  }

  /**
   * 投保
   */
  @PostMapping("/contracts")
  @ResponseStatus(HttpStatus.CREATED)
  public InsureOrder insured(@RequestBody InsureOrder insureOrder) {
    log.info("/contracts: {}", JSON.toJSONString(insureOrder));
    String policyNo = UUID.randomUUID().toString();
    final InsuranceContract contract = new InsuranceContract()
        .setPolicyNo(policyNo).setInsureNo(insureOrder.getInsureNo())
        .setCreatedAt(LocalDateTime.now());
    insureOrder.setPolicyNo(policyNo);
    insureOrder.setInsuranceContract(contract);
    return insureOrder;
  }

  /**
   * 报案
   */
  @PostMapping("/claims")
  @ResponseStatus(HttpStatus.CREATED)
  public Claim reportAccident(@RequestBody Claim claim) {
    log.info("/claims: {}", JSON.toJSONString(claim));
    // 保单号
    final String policyNo = claim.getInsureOrder().getInsuranceContract().getPolicyNo();
    final List<InsureOrder> insureOrders = queryInsureOrderByPolicyNo(policyNo);
    if (insureOrders.isEmpty()) {
      throw new IllegalArgumentException("policyNo=" + policyNo + ": 保单不存在");
    }
    // 保单号做幂等处理
    Claim dbData = queryClaimByPolicyNo(policyNo);
    if (dbData != null) {
      claim.setCarrierReportNo(dbData.getCarrierReportNo());
      return claim;
    }
    // todo 业务处理
    String reportNo = "reportNo: " + UUID.randomUUID().toString();
    claim.setCarrierReportNo(reportNo);
    return claim;
  }

  /**
   * 申请理赔
   */
  @PostMapping("/claims/{reportNo}/transactionList")
  @ResponseStatus(HttpStatus.CREATED)
  public void reportAccident(@PathVariable String reportNo, @RequestBody Claim claim) {
    log.info("/claims/{reportNo}/transactionList: {}, {}", reportNo, JSON.toJSONString(claim));
    // 报案号
    // todo 业务处理
  }

  /**
   * 保单号查理赔
   */
  private Claim queryClaimByPolicyNo(String policyNo) {
    return null;
  }


}
