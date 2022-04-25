package helpers;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import jp.ac.kobe_u.cs.cream.Solution;

public class CalculationHelper {

    public static String getAnswer(String equation) {
    //creating the answer for the equation for the given string

        EvalUtilities util = new EvalUtilities(false,true);
        try{
            IExpr result = util.evaluate(equation);
            return"$$"+ stringToJqmath(result.toString())+"$$</br>";
        }
        catch (SyntaxError e){
            Log.e("err",e.getMessage());
            return "Syntax error";
        }
        catch (MathException e){
            return "Math Error";
        }

    }
    public static String stringToJqmath(String equation)  {
        //Changing SymJa syntax to jqMath
        String FirstSwap=equation.replaceAll("Sqrt","√");
        String secondSwap=FirstSwap.replaceAll("Pow","^");
        String thirdSwap=secondSwap.replaceAll("\\{", "" );
        String fourthSwap=thirdSwap.replaceAll("\\}", "" );
        String fifth=fourthSwap.replaceAll("=", "" );
        return fifth.replaceAll(",","\\$\\$\\$\\$");

    }

}
