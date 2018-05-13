package com.qcloud.image;

import java.io.File;

import com.qcloud.image.request.FaceIdCardCompareRequest;

public class IdCardIdentityUtils {
	public static String faceIdCardCompare(ImageClient imageClient, String bucketName) {
		String ret;
		// 1. url鏂瑰紡
		System.out.println("====================================================");
		String idcardNumber = "420222198909103778";
		String idcardName = "鏉庢檽闇�";
		String idcardCompareUrl = "";
		String sessionId = "";
		/*
		 * FaceIdCardCompareRequest idCardCompareReq = new
		 * FaceIdCardCompareRequest(bucketName, idcardNumber, idcardName,
		 * idcardCompareUrl, sessionId);
		 * 
		 * ret = imageClient.faceIdCardCompare(idCardCompareReq);
		 * System.out.println("face idCard Compare ret:" + ret);
		 */
		// 2. 鍥剧墖鍐呭鏂瑰紡
		System.out.println("====================================================");
		String idcardCompareName = "";
		File idcardCompareImage = null;
		try {
			idcardCompareName = "idcard.jpg";
			idcardCompareImage = new File("idcard.jpg");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		FaceIdCardCompareRequest idCardCompareReq = new FaceIdCardCompareRequest(bucketName, idcardNumber, idcardName,
				idcardCompareName, idcardCompareImage, sessionId);
		ret = imageClient.faceIdCardCompare(idCardCompareReq);
		return ret;
//		System.out.println("face idCard Compare ret:" + ret);
	}
}
