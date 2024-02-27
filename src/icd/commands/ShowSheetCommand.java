package icd.commands;

import net.dirtt.icelib.main.commandmodules.exception.CommandException;
import net.dirtt.icelib.report.worksheet.ui.IceWorksheetFrame;
import net.dirtt.icelib.report.worksheet.main.IceWorksheet;
import net.dirtt.icelib.report.generalquote.icd.ICDQuote;
import net.dirtt.icelib.main.Solution;
import net.dirtt.icelib.main.commandmodules.Command;

public class ShowSheetCommand extends Command
{
    private Solution solution;
    
    public ShowSheetCommand(final Solution solution) {
        super(solution, false);
        this.solution = solution;
    }
    
    protected boolean doAction() throws CommandException {
        ICDQuote icdQuote = (ICDQuote)this.solution.getReport(33);
        if (icdQuote == null) {
            icdQuote = (ICDQuote)this.solution.buildUnCompiledReport(33);
        }
        if (icdQuote.isSuspendable()) {
            icdQuote.setSuspended(false, false);
        }
        final IceWorksheetFrame instance = IceWorksheetFrame.getInstance(this.solution, new IceWorksheet[] { icdQuote.createQuoteWorksheet(this.solution) });
        instance.setVisible(true);
        this.solution.fireModelEvents();
        instance.toFront();
        return false;
    }
}
