package interview.guide.common.ai;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 当前日期时间工具
 * 供 AI 模型调用以获取准确的当前日期时间
 */
@Component
public class CurrentDateTimeTool {

    /**
     * 获取当前日期时间（无参数版本）
     * @return 当前时间的格式化字符串
     */
    public String getCurrentDateTime() {
        return getCurrentDateTime(null);
    }

    /**
     * 获取当前日期时间
     * @param format 日期格式，如 "yyyy-MM-dd HH:mm" 或 "yyyy年MM月dd日"
     * @return 当前时间的格式化字符串
     */
    public String getCurrentDateTime(String format) {
        if (format == null || format.isBlank()) {
            format = "yyyy年MM月dd日 HH:mm";
        }
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return "当前时间：" + now.format(formatter);
        } catch (Exception e) {
            // 如果格式错误，使用默认格式
            LocalDateTime now = LocalDateTime.now();
            return "当前时间：" + now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm"));
        }
    }
}
