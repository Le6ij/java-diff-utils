package difflib;

import difflib.myers.Equalizer;
import difflib.myers.MyersDiff;

import java.util.List;
import java.util.Objects;

/**
 * This class is for generating a side-by-sidy HTML page where all differences will be highlighted for end-used to view.
 * <p>
 * @author <a href="aleksejs.bagajevs@gmail.com">Aleksejs Bagajevs</a>
 */
public class DiffHTMLGenerator {

    public DiffHTMLGenerator(Equalizer<String> diffAlgorithm) {
        this.diffAlgorithm = diffAlgorithm;
    }

    public DiffHTMLGenerator(){
        diffAlgorithm = DEFAULT_EQUALIZER;
    }
    private final Equalizer<String> diffAlgorithm;
    private final Equalizer<String> DEFAULT_EQUALIZER = new Equalizer<String>() {
        public boolean equals(final String original, final String revised) {
            if (original == null && revised == null) {
                return true;
            }
            if (original != null) {
                return original.equals(revised);
            }
            return false;
        }

        @Override
        public boolean skip(String original) {
            return false;
        }
    };

    public String buildSideBySideHTML(List<String> original, List<String> revised) {
        return buildSideBySideHTML(original, revised, DiffUtils.diff(original, revised, diffAlgorithm));
    }

    public String buildSideBySideHTML(List<String> original, List<String> revised,Patch<String> patch){
        String html = "<!DOCTYPE html>\n"
                +"<html prefix=\"og: http://ogp.me/ns#\">\n"
                +"<head>\n"
                +"	<title>Side-By-Side Test</title>\n"
                +"	<style type=\"text/css\">\n"
                +"	   table.diff {\n"
                +"	      border: 1px solid darkgray;\n"
                +"	      border-collapse: collapse;\n"
                +"	      min-width: 100%;\n"
                +"	      table-layout: fixed;\n"
                +"	      white-space: pre-wrap;\n"
                +"	   }\n"
                +"	   table.diff tbody {\n"
                +"	      font-family: Courier,monospace;\n"
                +"	   }\n"
                +"	   table.diff tbody th {\n"
                +"	      background: #eed none repeat scroll 0 0;\n"
                +"	      color: #886;\n"
                +"	      display: block;\n"
                +"	      font-family: verdana,arial,\"Bitstream Vera Sans\",helvetica,sans-serif;\n"
                +"	      font-size: 11px;\n"
                +"	      font-weight: normal;\n"
                +"	      padding: 0.3em 0.5em 0.1em 2em;\n"
                +"	      text-align: right;\n"
                +"	      vertical-align: top;\n"
                +"	   }\n"
                +"	   table.diff thead {\n"
                +"	      background: #efefef none repeat scroll 0 0;\n"
                +"	      border-bottom: 1px solid #bbc;\n"
                +"	      font-family: Verdana;\n"
                +"	   }\n"
                +"	   table.diff thead th.texttitle {\n"
                +"	      background-color: gray;\n"
                +"	      color: white;\n"
                +"	      font-size: 23px;\n"
                +"	      font-weight: lighter;\n"
                +"	      letter-spacing: 1px;\n"
                +"	      padding: 10px;\n"
                +"	      text-align: center;\n"
                +"	   }\n"
                +"	   table.diff tbody td {\n"
                +"	      padding: 0.4em 0.4em 0;\n"
                +"	      vertical-align: top;\n"
                +"	      word-break: break-all;\n"
                +"	   }\n"
                +"	   table.diff .empty {\n"
                +"	      background-color: #ddd;\n"
                +"	      width: 50%;\n"
                +"	   }\n"
                +"	   table.diff .move {\n"
                +"	      background-color: #bebebe;\n"
                +"	      width: 50%;\n"
                +"	   }\n"
                +"	   table.diff .change {\n"
                +"	      background-color: #fff900;\n"
                +"	      width: 50%;\n"
                +"	   }\n"
                +"	   table.diff .delete {\n"
                +"	      background-color: #e99;\n"
                +"	      width: 50%;\n"
                +"	   }\n"
                +"	   table.diff .skip {\n"
                +"	      -moz-border-bottom-colors: none;\n"
                +"	      -moz-border-left-colors: none;\n"
                +"	      -moz-border-right-colors: none;\n"
                +"	      -moz-border-top-colors: none;\n"
                +"	      background-color: #efefef;\n"
                +"	      border-color: #aaa #bbc #aaa #aaa;\n"
                +"	      border-image: none;\n"
                +"	      border-style: solid;\n"
                +"	      border-width: 1px;\n"
                +"	      width: 50%;\n"
                +"	   }\n"
                +"	   table.diff .insert {\n"
                +"	      background-color: #9e9;\n"
                +"	      width: 50%;\n"
                +"	   }\n"
                +"	   table.diff .equal {\n"
                +"	      width: 50%;\n"
                +"	   }\n"
                +"	   table.diff th.author {\n"
                +"	      background: #efefef none repeat scroll 0 0;\n"
                +"	      border-top: 1px solid #bbc;\n"
                +"	      text-align: right;\n"
                +"	   }\n"
                +"	</style>\n"
                +"</head>\n"
                +"<body id=\"cbBody\">\n"
                +"	<div class=\"innerBody setBgImage\" id=\"innerBody\">\n"
                +"		<div class=\"container-fluid viewerInnerBodyDiv\">\n"
                +"			<div class=\"row-fluid\">\n"
                +"				<div class=\"center\">\n"
                +"					<div class=\"span12 fileDiffMainContainer\">\n"
                +"						<div class=\"span12 difftable\" id=\"showDiff\" style=\"\">\n"
                +"							<table class=\"diff\">\n"
                +"								<thead>\n"
                +"									<tr>\n"
                +"										<th></th>\n"
                +"										<th class=\"texttitle\">Template</th>\n"
                +"										<th></th>\n"
                +"										<th class=\"texttitle\">Outgoing Request</th>\n"
                +"									</tr>\n"
                +"								</thead>\n"
                +"								<tbody>\n";
        Integer orig = 0;
        Integer rev = 0;

        while (orig<original.size() && rev<revised.size()){
            for (Delta d : patch.getDeltas()){
                if(orig==d.getOriginal().getPosition() && !d.getOriginal().getLines().isEmpty()){
                    if(d.getType().equals(Delta.TYPE.MOVE)){
                        for(Object ignored : d.getOriginal().getLines()){
                            html+="<tr><th>"+(orig+1)+"</th><td class=\"move\">"+Utils.htmlEntites(original.get(orig))+"</td><th></th><td class=\"empty\"></td></tr>";
                            orig++;
                        }
                        break;
                    }else if(d.getType().equals(Delta.TYPE.DELETE)){
                        for(Object ignored : d.getOriginal().getLines()){
                            html+="<tr><th>"+(orig+1)+"</th><td class=\"delete\">"+Utils.htmlEntites(original.get(orig))+"</td><th></th><td class=\"empty\"></td></tr>";
                            orig++;
                        }
                    }else if(d.getType().equals(Delta.TYPE.CHANGE)){
                        for(Object ignored : d.getOriginal().getLines()){
                            html+="<tr><th>"+(orig+1)+"</th><td class=\"change\">"+Utils.htmlEntites(original.get(orig))+"</td><th>"+(rev+1)+"</th><td class=\"change\">"+Utils.htmlEntites(revised.get(rev))+"</td></tr>";
                            orig++;
                            rev++;
                        }
                    }
                }
                else if (rev==d.getRevised().getPosition() && !d.getRevised().getLines().isEmpty()){
                    if(d.getType().equals(Delta.TYPE.MOVE)){
                        for(Object ignored : d.getRevised().getLines()){
                            html+="<tr><th></th><td class=\"empty\"></td><th>"+(rev+1)+"</th><td class=\"move\">"+Utils.htmlEntites(revised.get(rev))+"</td></tr>";
                            rev++;
                        }
                        break;
                    }else if(d.getType().equals(Delta.TYPE.INSERT)){
                        for(Object ignored : d.getRevised().getLines()){
                            html+="<tr><th></th><td class=\"empty\"></td><th>"+(rev+1)+"</th><td class=\"insert\">"+Utils.htmlEntites(revised.get(rev))+"</td></tr>";
                            rev++;
                        }
                    }
                }
            }
            html+="<tr style=\"display: table-row;\"><th>"+(orig+1)+"</th><td class=\"equal\">"+ Utils.htmlEntites(original.get(orig))+"</td><th>"+(rev+1)+"</th><td class=\"equal\">"+Utils.htmlEntites(revised.get(rev))+"</td></tr>\n";
            orig++;
            rev++;
        }
        html+="								</tbody>\n"
                +"							</table>\n"
                +"						</div>\n"
                +"			</div>\n"
                +"				</div>\n"
                +"			</div>\n"
                +"		</div>\n"
                +"	</div>\n"
                +"</body>\n"
                +"</html>\n";
        return html;
    }
}
