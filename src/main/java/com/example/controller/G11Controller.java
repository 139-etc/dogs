package com.example.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.CommonConstants;
import com.example.CommonParts;
import com.example.domain.dog.model.Breed;
import com.example.domain.dog.model.Result;
import com.example.domain.dog.service.DogService;
import com.example.domain.user.model.MUser;
import com.example.domain.user.service.UserService;

@Controller
public class G11Controller {

	@Autowired
	private DogService dogService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommonParts commonParts;

	@Autowired
	HttpServletResponse response;

	/** ユーザログ画面を表示 */
	@GetMapping("/admin/G11")
	public String getInit(Model model, HttpSession session) {
		
		//選択肢のために犬種情報をモデルに詰める
		List<Breed> breeds = dogService.getBreeds();
		model.addAttribute("breeds", breeds);

		//複数ユーザのリザルトのための入れ物を作る
		List<Result> resultsSum = new ArrayList<Result>();

		//セッションからユーザIDのリストを取り出す
		List<String> userIds = (List<String>) session.getAttribute("userIds");

		//ユーザIDのリストからユーザIDを取り出す
		for (String userId : userIds) {
			//ユーザIDからリザルトのリストを取り出す
			List<Result> results = dogService.getResult(userId);

			//リザルトのリストをまとめる
			resultsSum.addAll(results);
		}
		
	    //表示用としてそのまま渡す
	    model.addAttribute("resultsSum", resultsSum);

		return "/admin/G11";
	}

	/** ログを削除 */
	@PostMapping(value = "/admin/G11", params = "deleteLog")
	public String deleteLog(@RequestParam(value = "ids", required = false) List<String> ids, Model model,
			HttpSession session) {
		
		if (ids == null || ids.isEmpty()) {
			//エラーメッセージの中身用の入れ物を作る
			List<String> emList = new ArrayList<String>();
			emList.add(CommonConstants.EM_E01);

			//モーダル起動の変数とエラーメッセージの中身をセッションに詰める
			commonParts.setModal(session, emList);
			
			return "redirect:/admin/G11";
		}

		//ユーザIDのリストからユーザIDを取り出す
		for (String id : ids) {
			//IDをもとに削除
			dogService.deleteResult(id);
		}

		return "redirect:/admin/G11";
	}

	/** ログを更新 */
	@PostMapping(value = "/admin/G11", params = "updateLog")
	public String updateLog(@ModelAttribute("results") ArrayList<Result> results,
			@RequestParam(value = "ids", required = false) List<String> ids, 
			Model model,HttpSession session) {

		//エラーメッセージの中身用の入れ物を作る
		List<String> emList = new ArrayList<String>();
		
		if (ids == null || ids.isEmpty()) {
			//モーダル起動の変数とエラーメッセージの中身をセッションに詰める
			emList.add(CommonConstants.EM_E01);
			commonParts.setModal(session, emList);
			
			return "redirect:/admin/G11";
		}
		
	    for (Result result : results) {
	    	//検証用のboolean値を用意
	    	boolean check = false;
	    	
	        // チェックされていない行はスキップ
	        if (ids.contains(result.getId())) {
	        	
	    		if (result.getBreed() == null || result.getBreed().equals("")) {
	    			check = true;
	    			emList.add(CommonConstants.EM_E06.replace("{1}", "犬種"));
	    		}

	    		if (result.getStamina() == null || result.getStamina().equals("")){
	    			check = true;
	    			emList.add(CommonConstants.EM_E06.replace("{1}", "体力"));
	    		}

	    		if (result.getPoint() == null || result.getPoint().equals("")) {
	    			check = true;
	    			emList.add(CommonConstants.EM_E06.replace("{1}", "得点"));
	    		}

	        	if(check == false) {
		        	dogService.updateResult(result);
	        	}
	        }
	    }

		return getInit(model,session);
	}

	
	@PostMapping(value="/admin/G11", params = "export")
	public String exportCsv(Model model, HttpSession session) throws IOException {

		//リザルトの集計のリストの入れ物を作る
		List<Result> resultsSum = new ArrayList<Result>();
		
		//セッションからユーザIDのリストを取り出す
		List<String> userIds = (List<String>) session.getAttribute("userIds");
		
		//ユーザIDのリストからユーザIDを取り出す
		for (String userId : userIds) {
			//ユーザIDからリザルトのリストを取り出す
			List<Result> results = dogService.getResult(userId);

			//リザルトのリストをまとめる
			resultsSum.addAll(results);
		}

		if (resultsSum != null && !resultsSum.isEmpty()) {
			//ファイル名に使うユーザ名を使う
			String fileUID = "";
			
			for(String userId : userIds) {
				fileUID = fileUID + userId + "&";
			}
			
			//末尾の＆を削除
			fileUID = fileUID.replaceAll("&$", "");
			
			//ファイル名を設定
			String fileName = "Results_" + fileUID + "_" + LocalDateTime.now() + ".csv";

			//レスポンス設定
			//ファイル形式をCSVで指定
			response.setContentType("text/csv");
			//文字コードを指定
			response.setCharacterEncoding("UTF-8");
			//画面表示せずダウンロード、ファイル名を指定
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			//リザルト結果を拡張for文で書き込む
			PrintWriter writer = response.getWriter();

			for (Result res : resultsSum) {
				writer.println(
						res.getId() + "," +
						res.getUserId() + "," +
								res.getUserName() + "," +
								res.getBreed() + "," +
								res.getStamina() + "," +
								res.getPoint() + "," +
								res.getStartedTime() + "," +
								res.getUpdateTime());
			}

			writer.close();

		}

		//G11画面に遷移		
		return "redirect:/admin/G11";
	}

	@PostMapping(value="/admin/G11", params = "import")
	public String importCsv(@RequestParam("file") MultipartFile file, Model model, HttpSession session)
			throws UnsupportedEncodingException, IOException, DuplicateKeyException {
		
		//ファイルの中身が空だったら戻る
		if (file.isEmpty()) {
			//エラーメッセージの中身用の入れ物を作る
			List<String> emList = new ArrayList<String>();
			emList.add(CommonConstants.EM_E02);

			//モーダル起動の変数とエラーメッセージの中身をセッションに詰める
			commonParts.setModal(session, emList);

			return "redirect:/admin/G11";
		}

		InputStreamReader input = new InputStreamReader(file.getInputStream(), "UTF-8");
		BufferedReader br = new BufferedReader(input);

		String line = null;

		//行数を表す
		int i = 1;

		//検証用のMUserのリストを作っておく
		//ユーザIDのリスト
		List<String> userIds = new ArrayList<String>();
		//ユーザ名のリスト
		List<String> userNames = new ArrayList<String>();

		for (MUser user : userService.getUsers(null)) {
			userIds.add(user.getUserId());
			userNames.add(user.getUserName());
		}
		
		//犬種名とその体力の最大値のマップを作っておく
		Map<String,Integer> breedInfo = new HashMap<String,Integer>();
		
		for (Breed breed : dogService.getBreeds()) {
			breedInfo.put(breed.getName(), breed.getStamina());
		}

		//次の行がなくなるで続ける
		while ((line = br.readLine()) != null) {
			//1行からカンマで区切ったListを作る
			List<String> record = Arrays.asList(line.split(","));

			//エラーメッセージの中身用の入れ物を作る
			List<String> emList = new ArrayList<String>();

			//今の行数であらかじめエラーが起きるように仕込んでおく
			emList.add(CommonConstants.EM_E03.replace("{1}", String.valueOf(i)));

			//レコードのサイズを確認
			if (record.size() != 8) {
				emList.add(CommonConstants.EM_E04);
			} else {
				//パターン検証のboolean変数を用意
				boolean patternCheck = false;

				//レコードのパターン検証
				if(record.get(0) == null || record.get(0).equals("")) {
					patternCheck = true;
					emList.add(CommonConstants.EM_E05.replace("{1}", "ID"));
				}
				
				if (record.get(1) == null || record.get(1).equals("") || 
						!userIds.contains(record.get(1))) {
					patternCheck = true;
					emList.add(CommonConstants.EM_E05.replace("{1}", "ユーザID"));
				}

				if (record.get(2) == null || record.get(2).equals("") || 
						!userNames.contains(record.get(2))) {
					patternCheck = true;
					emList.add(CommonConstants.EM_E05.replace("{1}", "ユーザ名"));
				}

				if (record.get(3) == null || record.get(3).equals("") || 
						!breedInfo.containsKey(record.get(3))) {
					patternCheck = true;
					emList.add(CommonConstants.EM_E05.replace("{1}", "犬種"));
				}

				if (record.get(4) == null || record.get(4).equals("") || 
						!record.get(4).matches("^[0-9]{2,3}$") || 
						breedInfo.get(record.get(3)) == null || 
						(Integer.compare(
								breedInfo.get(record.get(3)).intValue(),
								Integer.parseInt(record.get(4)
										))
						== 1)) {
					patternCheck = true;
					emList.add(CommonConstants.EM_E05.replace("{1}", "体力"));
				}

				if (record.get(5) == null || record.get(5).equals("") || 
						!record.get(5).matches("^(100|[1-9]?[0-9])$")) {
					patternCheck = true;
					emList.add(CommonConstants.EM_E05.replace("{1}", "得点"));
				}

				if (record.get(6) == null || record.get(6).equals("") || 
						!record.get(6).matches(
						"^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) (0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d)\\.\\d{3}$")) {
					patternCheck = true;
					emList.add(CommonConstants.EM_E05.replace("{1}", "開始時刻"));
				}

				if (record.get(7) == null || record.get(7).equals("") || 
						!record.get(7).matches(
						"^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) (0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d)\\.\\d{3}$")) {
					patternCheck = true;
					emList.add(CommonConstants.EM_E05.replace("{1}", "更新日時"));
				}

				if (patternCheck == false) {
					//ユーザ名と更新日時から同じレコードがないかチェック
					if (dogService.checkDeplicateResult(record.get(0), record.get(7)) == 0) {
						dogService.importResult(record);
						
						//先に設定しておいたリストから削除
						emList.remove(0);
					} else {
						//同じレコードが存在する場合
						//エラーメッセージの中身用の入れ物を作る
						emList.add(CommonConstants.EM_E06.replace("{1}", "レコード"));
					}
				}
			}
			
			//モーダル起動の変数とエラーメッセージの中身をセッションに詰める
			commonParts.setModal(session, emList);

			//次の行のために1増やす
			i++;
		}
		return "redirect:/admin/G11";
	}
}
