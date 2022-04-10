package com.pi.math_vision_android;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

public class Racunanje {
    public String getAnswer(String s) {

        EvalUtilities util = new EvalUtilities(false,true);
        try{
            IExpr result = util.evaluate(s);
            return result.toString();
        }
        catch (SyntaxError e){
            Log.e("err",e.getMessage());
            return "Syntax error";
        }
        catch (MathException e){
            return "Math Error";
        }

    }

}
