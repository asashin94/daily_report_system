package models.validators;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.ForwardConst;
import constants.MessageConst;
import services.ReportService;


/**
 * 日報インスタンスに設定されている値のバリデーションを行うクラス
 */
public class ReportValidator {

    /**
     * 日報インスタンスの各項目についてバリデーションを行う
     * @param rv 日報インスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(HttpServletRequest request,ReportView rv,EmployeeView ev) {
        List<String>errors = new ArrayList<String>();

        //タイトルのチェック
        String titleError = validateTitle(rv.getTitle());
        if (!titleError.equals("")) {
            errors.add(titleError);
        }

        //内容のチェック
        String contentError = validateContent(rv.getContent());
        if (!contentError.equals("")) {
            errors.add(contentError);
        }

        //【追記】出勤時間のチェック
        String goAtError = validateGoAt(rv.getGoAt());
        if (!goAtError.equals("")) {
            errors.add(goAtError);
        }

        //【追記】退勤時間のチェック
        String leaveAtError = validateLeaveAt(rv.getLeaveAt());
        if (!leaveAtError.equals("")) {
            errors.add(leaveAtError);
        }

        //【追記】日付のチェック
        String createdAtError = validateCreatedAt(request,rv.getReportDate(), ev);
        if (ev != null) {
            if (!createdAtError.equals("")) {
                errors.add(createdAtError);
            }
        }
        //【追記】出勤時間が退勤時間より遅くないかチェック
        String slowError=validateSlow(rv.getGoAt(),rv.getLeaveAt());
        if(!slowError.equals("")) {
            errors.add(slowError);
        }
        return errors;

    }

    /**
     * タイトルに入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param title タイトル
     * @return エラーメッセージ
     */
    private static String validateTitle(String title) {
        if (title==null||title.equals("")) {
            return MessageConst.E_NOTITLE.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param content 内容
     * @return エラーメッセージ
     */
    private static String validateContent(String content){
        if (content==null||content.equals("")){
            return MessageConst.E_NOCONTENT.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 【追記】createかをチェックし、createであれば同一日チェックを実施する
     */
    @SuppressWarnings({ "unlikely-arg-type" })
    private static String validateCreatedAt(HttpServletRequest request,LocalDate createdAt,EmployeeView employeeView) {
        if(request.getParameter(ForwardConst.ACT.getValue()).equals(ForwardConst.CMD_CREATE)) {
            ReportService service = new ReportService();
            if(createdAt.equals(service.newCreatedAt(employeeView))) {
                return MessageConst.E_SAMEDATE.getMessage();
            }
        }
        return "";
    }

    /**
     * 【追記】 出勤時間が退勤時間より遅くないかをチェックし、遅ければエラーメッセージを返却
     */
    private static String validateSlow(LocalDateTime goAt,LocalDateTime leaveAt) {
        if(goAt!=null && leaveAt!=null && goAt.isAfter(leaveAt)) {
            return MessageConst.E_SLOW.getMessage();
        }
        return "";
    }


    /**
     * 【追記】内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param goAt 出勤時間
     * @return エラーメッセージ
     */
    private static String validateGoAt(LocalDateTime goAt){

        if (goAt == null) {
         return MessageConst.E_NOGOAT.getMessage();
        }
        return "";
    }
    /**
     * 【追記】内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param leaveAt 退勤時間
     * @return エラーメッセージ
     */
    private static String validateLeaveAt(LocalDateTime leaveAt){
        if (leaveAt==null){
            return MessageConst.E_NOLEAVEAT.getMessage();
        }
        return "";
    }

}