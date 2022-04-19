package com.pi.math_vision_android;

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

public class Racunanje {
    public String getAnswer(String s) {

        EvalUtilities util = new EvalUtilities(false,true);
        try{
            IExpr result = util.evaluate(s);
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
    public String stringToJqmath(String solution)  {
        //pretvaranje sintakse od SymJA-e  u sintaksu od JQmatha
        String prvaZamjena=solution.replaceAll("Sqrt","√");
        String DrugaZamjena=prvaZamjena.replaceAll("Pow","^");
        String trecaZamjena=DrugaZamjena.replaceAll("\\{", "" );
        String Cetvrtazamjena=trecaZamjena.replaceAll("\\}", "" );
        return Cetvrtazamjena.replaceAll(",","\\$\\$\\$\\$");

    }

}
