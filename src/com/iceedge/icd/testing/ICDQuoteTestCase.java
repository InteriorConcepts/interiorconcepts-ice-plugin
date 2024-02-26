// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.testing;

import net.dirtt.icelib.report.generalquote.icd.ICDQuote;
import net.dirtt.icelib.report.generalquote.GeneralQuote;
import java.io.File;
import net.dirtt.icelib.main.Solution;
import net.iceedge.test.icelib.quote.TestQuote.furniture.GeneralQuoteTestCase;

public class ICDQuoteTestCase extends GeneralQuoteTestCase
{
    public ICDQuoteTestCase(final Solution solution, final File file, final File file2, final File file3, final File file4) {
        super(solution, file, file2, file3, file4);
    }
    
    protected GeneralQuote getUncompiledQuote(final Solution solution) {
        ICDQuote icdQuote = (ICDQuote)solution.getReport(33);
        if (icdQuote == null) {
            icdQuote = (ICDQuote)solution.buildUnCompiledReport(33);
        }
        return icdQuote;
    }
}
