package de.ovgu.featureide.core.mpl.signature.fuji;

import de.ovgu.featureide.core.mpl.signature.abstr.AbstractClassFragment;
import de.ovgu.featureide.core.mpl.signature.abstr.AbstractFieldSignature;
import de.ovgu.featureide.core.mpl.signature.abstr.AbstractMethodSignature;
import de.ovgu.featureide.core.mpl.signature.abstr.AbstractSignature;

public class FujiStringBuilder {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public static String getClassString(AbstractClassFragment cls, boolean shortString) {
		final StringBuilder sb = new StringBuilder();
		
		if (!cls.getSignature().getPackage().isEmpty()) {
			sb.append("package ");
			sb.append(cls.getSignature().getPackage());
			sb.append(';');
			sb.append(LINE_SEPARATOR);
		}
		
		if (!cls.getSignature().getImportList().isEmpty()) {
			for (String importClass : cls.getSignature().getImportList()) {
				sb.append(importClass);
				sb.append(LINE_SEPARATOR);
			}
			sb.append(LINE_SEPARATOR);
		}
		
		sb.append(cls.getSignature().toString());

		if (!cls.getSignature().getExtendList().isEmpty()) {
			if (shortString) {
				sb.append(LINE_SEPARATOR);
				sb.append("\t\textends ");
			} else {
				sb.append(" extends ");				
			}
			for (String ext : cls.getSignature().getExtendList()) {
				sb.append(ext);	
				sb.append(", ");	
			}
			sb.delete(sb.length() - 2, sb.length());
		}

		if (!cls.getSignature().getImplementList().isEmpty()) {
			if (shortString) {
				sb.append(LINE_SEPARATOR);
				sb.append("\t\timplements ");
			} else {
				sb.append(" implements ");				
			}
			for (String impl : cls.getSignature().getImplementList()) {
				sb.append(impl);	
				sb.append(", ");
			}
			sb.delete(sb.length() - 2, sb.length());
		}
		
		sb.append(" {");
		sb.append(LINE_SEPARATOR);
		
		if (!cls.getInnerClasses().isEmpty()) {
			for (AbstractClassFragment innerClass : cls.getInnerClasses().values()) {
				sb.append('\t');
				String innerClassString;
				if (shortString) {
					innerClassString = innerClass.toShortString();
				} else {
					innerClassString = innerClass.toString();
				}
				sb.append(innerClassString.replace(LINE_SEPARATOR, LINE_SEPARATOR + '\t'));
				sb.append(LINE_SEPARATOR);
			}
			sb.append(LINE_SEPARATOR);
		}
		
		if (!cls.getMembers().isEmpty()) {
			for (AbstractSignature member : cls.getMembers()) {
				sb.append("\t");
				sb.append(member.toString().replace(LINE_SEPARATOR, LINE_SEPARATOR + '\t'));
				if (member instanceof AbstractFieldSignature) {
					AbstractFieldSignature field = (AbstractFieldSignature) member;
					if (shortString || !field.isFinal()) {
						sb.append(';');
					} else {
						sb.append(getFinalFieldInit(field));
					}
				} else if (member instanceof AbstractMethodSignature) {
					AbstractMethodSignature method = (AbstractMethodSignature) member;
					if (shortString || !"class".equals(cls.getSignature().getType())) {
						sb.append(';');
					} else {
						sb.append(" {");
						sb.append(LINE_SEPARATOR);
						
						//TODO MPL: use Fuji
						if (method.isConstructor()) {
							sb.append("\t\tsuper();");
						}
						if (!method.isConstructor()) {
							sb.append("\t\t" + getReturnStatement(method));
						}

						sb.append(LINE_SEPARATOR);
						sb.append("\t}");
					}
				}
				sb.append(LINE_SEPARATOR);
			}
			sb.append(LINE_SEPARATOR);
		}
		sb.append(LINE_SEPARATOR);
		sb.append('}');
		
		return sb.toString();
	}
	
	private static String getFinalFieldInit(AbstractFieldSignature field) {
		return " = " + getTypeDefaultValue(field);
	}

	private static String getReturnStatement(AbstractMethodSignature method) {
		return "return " + getTypeDefaultValue(method);
	}

	private static String getTypeDefaultValue(AbstractSignature element) {
		String type = element.getType();
		if (type.equals("void")) {
			return ";";
		} else if (type.equals("boolean")) {
			return "true;";
		} else if (type.equals("int") 
				|| type.equals("double") 
				|| type.equals("char")
				|| type.equals("long") 
				|| type.equals("float")  
				|| type.equals("byte")
				|| type.equals("short"))  {
			return "0;";
		} else {
			return "null;";
		}
	}
}