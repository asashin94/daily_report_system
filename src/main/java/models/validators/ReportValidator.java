package models.validators;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import actions.views.EmployeeView;
import actions.views.ReportView;
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
    public static List<String> validate(ReportView rv,EmployeeView ev) {
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

        //【追記】日付のチェック
        String createdAtError = validateCreatedAt(rv.getReportDate(), ev);
        if (!createdAtError.equals("")) {
            errors.add(createdAtError);
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
     *【追記】 内容に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param localDateTime
     * @return エラーメッセージ
     */
    private static String validateCreatedAt(LocalDate createdAt,EmployeeView employeeView) {
        ReportService service = new ReportService();
        if(createdAt.equals(service.newCreatedAt(employeeView))) {
            return MessageConst.E_SAMEDATE.getMessage();
        }
        return "";
    }

}