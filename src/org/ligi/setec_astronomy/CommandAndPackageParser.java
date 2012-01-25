package org.ligi.setec_astronomy;

/**
 * parses the package end the command of a Android Log snipped
 * 
 * @author ligi
 *
 */
class CommandAndPackageParser {
		private String command="";
		private String pkg="";
		boolean parsing_pkg=false;
		
		public CommandAndPackageParser(String log_line) {
			
			parse(log_line.trim());
			
			// postprocess the package name
			if (!pkg.contains(".")) // no point -> not a good package name
				command="INVALID";
			
			pkg=pkg.replace("Starting ","");
			
			if (pkg.contains(":"))
				pkg=pkg.substring(pkg.lastIndexOf(":")+1);
			
			if (pkg.contains(","))
				pkg=pkg.substring(0,pkg.indexOf(","));
			
			if (pkg.contains("/"))
				pkg=pkg.substring(0,pkg.indexOf("/"));
			
			if (pkg.equals("org.ligi.setec_astronomy")) // we do not want to process our selves 
				command="INVALID";
		}

		private void parse(String log_line) {
			
		    if (log_line.startsWith("Window Window")) 
		    	command="INVALID"; // they do not belong to a pkg
		    else if (log_line.startsWith("Starting:")) {
				command="Starting";
				parse_pkg(log_line,"cmp=");
			}
			else if (log_line.startsWith("CREATE SURFACE")) {
				command="CREATE SURFACE";
				parse_pkg(log_line,"name=");
			}

			else
				parse_default(log_line);
		}
		
		private void parse_pkg(String log_line,String pkg_starter) {
			int pkg_start=log_line.indexOf(pkg_starter);
			log_line=log_line.substring(pkg_start+pkg_starter.length());
			int pkg_end=log_line.length();
			
			if (log_line.contains("/"))
				pkg_end=Math.min(pkg_end, log_line.lastIndexOf("/"));

			if (log_line.contains(","))
				pkg_end=Math.min(pkg_end, log_line.lastIndexOf(","));

			pkg=log_line.substring(0,pkg_end);
		}

		private void parse_default(String log_line) {
			
			String snip="";
			
			for (int i=0;i<log_line.length();i++)
				switch(log_line.charAt(i)) {
				case ' ':
					if (parsing_pkg) {
						pkg+=snip;
						return;
					}
						
					command=getCommand() + (snip+" ");
					snip="";
					break;
	
				case '{':
					parsing_pkg=true;
					if (log_line.indexOf("act=")!=-1)
						
						parse(log_line.substring(log_line.indexOf("act=")+4 ));
					return;

				case '/':
					
					if (parsing_pkg) {
						pkg+=snip;
						return;
					}
					break;
					
				case '.':
					parsing_pkg=true;
					pkg+=snip+".";
					snip="";
					break;
	
				default:
					snip+=log_line.charAt(i);
					break;
				}
		}

		public String getCommand() {
			return command;
		}

		public String getPackage() {
			return pkg;
		}
		
	}