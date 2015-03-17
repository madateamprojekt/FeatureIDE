package de.ovgu.featureide.ui.statistics.core.composite.lazyimplementations;

import java.util.LinkedList;

import de.ovgu.featureide.core.fstmodel.FSTArbitraryRole;
import de.ovgu.featureide.core.fstmodel.FSTClass;
import de.ovgu.featureide.core.fstmodel.FSTClassFragment;
import de.ovgu.featureide.core.fstmodel.FSTFeature;
import de.ovgu.featureide.core.fstmodel.FSTInvariant;
import de.ovgu.featureide.core.fstmodel.FSTField;
import de.ovgu.featureide.core.fstmodel.FSTMethod;
import de.ovgu.featureide.core.fstmodel.FSTModel;
import de.ovgu.featureide.core.fstmodel.FSTRole;
import de.ovgu.featureide.ui.statistics.core.composite.lazyimplementations.genericdatatypes.AbstractSortModeNode;

/**
 * displays the sums of the different components in the statistics
 * 
 * @author Schleicher Miro
 */
public class SumImplementationArtifactsParent extends AbstractSortModeNode {
	
	public static final int NUMBER_OF_CLASSES = 0;
	public static final int NUMBER_OF_FIELDS = 1;
	public static final int NUMBER_OF_METHODS = 2;

	public static final int NUMBER_OF_INVARIANTS = 3;
	public static final int NUMBER_OF_CONTRACTS = 4;
	public static final int NUMBER_OF_CONTRACTS_METHODS = 5;
	public static final int NUMBER_OF_FEATURE_CONTRACTS = 6;
	public static final int NUMBER_OF_LINES = 7;

	private final FSTModel fstModel;
	private final int type;

	public SumImplementationArtifactsParent(String description, FSTModel fstModel, int type) {
		super(description);
		this.fstModel = fstModel;
		this.type = type;
	}

	@Override
	protected void initChildren() {

		if (type == NUMBER_OF_CLASSES) {
			for (FSTClass currClass : fstModel.getClasses()) {
				for (LinkedList<FSTClassFragment> classFragmentList : currClass.getAllFSTFragments()) {
					if (!classFragmentList.isEmpty()) {
						final FSTClassFragment firstClassFragment = classFragmentList.get(0);
						final FSTRole fstRole = classFragmentList.get(0).getRole();
						if (fstRole instanceof FSTArbitraryRole) {
							addChild(new ClassNodeParent(firstClassFragment.getName() + ": " + classFragmentList.size(), firstClassFragment, fstModel));
						} else {
							addChild(new ClassNodeParent(firstClassFragment.getFullIdentifier() + ": " + classFragmentList.size(), firstClassFragment, fstModel));
						}
					}
				}
			}
		} else if (type == NUMBER_OF_FIELDS) {
			LinkedList<FSTField> allFields = new LinkedList<FSTField>();
			for (FSTClass currClass : fstModel.getClasses()) {
				for (LinkedList<FSTClassFragment> classFragmentList : currClass.getAllFSTFragments()) {
					for (FSTClassFragment fstFrag : classFragmentList) {
						allFields.addAll(fstFrag.getFields());
					}
				}
			}
			while (allFields.size() > 0) {
				addChild(new FieldNodeParent(allFields.get(0).getFullIdentifier() + " : " + allFields.get(0).getType(), allFields.get(0), allFields));
				int pointer = 0;
				String fullI = allFields.get(0).getFullIdentifier();
				while (pointer < allFields.size()) {
					if (allFields.get(pointer).getFullIdentifier().equals(fullI)) {
						allFields.remove(pointer);
						pointer--;
					}
					pointer++;
				}
			}

		} else if (type == NUMBER_OF_METHODS) {
			LinkedList<FSTMethod> allMethods = new LinkedList<FSTMethod>();
			for (FSTClass currClass : fstModel.getClasses()) {
				for (LinkedList<FSTClassFragment> classFragmentList : currClass.getAllFSTFragments()) {
					for (FSTClassFragment fstFrag : classFragmentList) {
						allMethods.addAll(fstFrag.getMethods());
					}
				}
			}
			while (allMethods.size() > 0) {
				addChild(new MethodNodeParent(allMethods.get(0).getFullIdentifier() + " : " + allMethods.get(0).getType(), allMethods.get(0), allMethods));
				int pointer = 0;
				String fullI = allMethods.get(0).getFullIdentifier();
				while (pointer < allMethods.size()) {
					if (allMethods.get(pointer).getFullIdentifier().equals(fullI)) {
						allMethods.remove(pointer);
						pointer--;
					}
					pointer++;
				}
			}
		} else if (type == NUMBER_OF_INVARIANTS) {
			LinkedList<FSTInvariant> allInvariants = new LinkedList<FSTInvariant>();

			for (FSTClass currClass : fstModel.getClasses()) {
				for (LinkedList<FSTClassFragment> iterable_element : currClass.getAllFSTFragments()) {
					for (FSTClassFragment fstFrag : iterable_element) {
						allInvariants.addAll(fstFrag.getInvariants());
					}
				}
			}
			while (allInvariants.size() > 0) {
				addChild(new InvariantNodeParent(allInvariants.get(0).getRole().getClassFragment().getFullIdentifier(), allInvariants.get(0), allInvariants));
				int pointer = 0;
				String fullI = allInvariants.get(0).getRole().getClassFragment().getFullIdentifier();
				while (pointer < allInvariants.size()) {
					if (allInvariants.get(pointer).getRole().getClassFragment().getFullIdentifier().equals(fullI)) {
						allInvariants.remove(pointer);
						pointer--;
					}
					pointer++;
				}
			}
		} else if (type == NUMBER_OF_CONTRACTS) {

			LinkedList<FSTMethod> allMethodContracts = new LinkedList<FSTMethod>();

			allMethodContracts = getAllMethodsContractsList();

			while (allMethodContracts.size() > 0) {
				addChild(new MethodContractNodeParent(allMethodContracts.get(0).getRole().getClassFragment().getFullIdentifier(), allMethodContracts.get(0),
						allMethodContracts));
				int pointer = 0;
				String fullI = allMethodContracts.get(0).getRole().getClassFragment().getFullIdentifier();
				while (pointer < allMethodContracts.size()) {
					if (allMethodContracts.get(pointer).getRole().getClassFragment().getFullIdentifier().equals(fullI)) {
						allMethodContracts.remove(pointer);
						pointer--;
					}
					pointer++;
				}

			}
		} else if (type == NUMBER_OF_CONTRACTS_METHODS) {

			LinkedList<FSTMethod> allContractsMethod = new LinkedList<FSTMethod>();

			allContractsMethod = getAllMethodsContractsList();

			while (allContractsMethod.size() > 0) {
				addChild(new MethodContractNodeParent(allContractsMethod.get(0).getFullIdentifier()
						.substring(0, allContractsMethod.get(0).getFullIdentifier().lastIndexOf(allContractsMethod.get(0).getName()))
						+ allContractsMethod.get(0).getFullName(), allContractsMethod.get(0), allContractsMethod));
				int pointer = 0;
				String fullI = allContractsMethod.get(0).getFullIdentifier();
				while (pointer < allContractsMethod.size()) {
					if (allContractsMethod.get(pointer).getFullIdentifier().equals(fullI)) {
						allContractsMethod.remove(pointer);
						pointer--;
					}
					pointer++;
				}

			}

		} else if(type == NUMBER_OF_FEATURE_CONTRACTS){
			
			LinkedList<FSTMethod> allContractsFeature = new LinkedList<FSTMethod>();

			allContractsFeature = getAllMethodsContractsList();
			
			while (allContractsFeature.size() > 0) {
				addChild(new FeatureContractNodeParent(allContractsFeature.get(0).getRole().getFeature().getName(), allContractsFeature.get(0), allContractsFeature));
				int pointer = 0;
				String fullI = allContractsFeature.get(0).getRole().getFeature().getName();
				while (pointer < allContractsFeature.size()) {
					if (allContractsFeature.get(pointer).getRole().getFeature().getName().equals(fullI)) {
						allContractsFeature.remove(pointer);
						pointer--;
					}
					pointer++;
				}

			}
			
			
		}

	}

	private LinkedList<FSTMethod> getAllMethodsContractsList() {
		LinkedList<FSTMethod> allMethodContracts = new LinkedList<FSTMethod>();

		for (FSTClass currClass : fstModel.getClasses()) {
			for (LinkedList<FSTClassFragment> iterable_element : currClass.getAllFSTFragments()) {
				for (FSTClassFragment fstFrag : iterable_element) {
					for (FSTMethod method : fstFrag.getMethods()) {
						if (method.hasContract()) {
							allMethodContracts.add(method);
						}
					}

				}
			}
		}
		return allMethodContracts;
	}

}
