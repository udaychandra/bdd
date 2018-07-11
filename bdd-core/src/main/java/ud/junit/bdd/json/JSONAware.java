/*
 * Redistributing "JSON.simple" library. Please visit https://github.com/fangyidong/json-simple
 *
 * Minor modification: using StringBuilder.
 */

package ud.junit.bdd.json;

/**
 * Beans that support customized output of JSON text shall implement this interface.  
 * @author FangYidong<fangyidong@yahoo.com.cn>
 */
public interface JSONAware {
	/**
	 * @return JSON text
	 */
	String toJSONString();
}
