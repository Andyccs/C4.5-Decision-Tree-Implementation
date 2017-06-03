/*********************************
 * Author: Xue (Charlotte) Lin
 * Date: 2015/04/01
 *********************************/
package C45CoreAlgorithm;

import DataDefination.Attribute;
import DataDefination.Instance;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;

public class InfoGainContinuous {

  private Attribute attribute;
  private double threshold;
  private double infoGain = -1;
  private HashMap<String, ArrayList<Instance>> subset;

  /**
   * Constructor: initialize fields. This class is for calculating the information gain
   * of continuous attribute.
   * Use one cut to binary method.
   */
  public InfoGainContinuous(Attribute attribute, Attribute target,
      ArrayList<Instance> instances) throws IOException {

    this.attribute = attribute;

    // Initialize threshold and infoGain
    // (1) Get the name of the attribute to be calculated
    final String attributeName = attribute.getName();

    // (2) Sort instances according to the attribute
    Comparator<Instance> comparator = new Comparator<Instance>() {
      @Override
      public int compare(Instance x, Instance y) {
        HashMap<String, String> xPair = x.getAttributeValuePairs();
        String xValue = xPair.get(attributeName);

        HashMap<String, String> yPair = y.getAttributeValuePairs();
        String yValue = yPair.get(attributeName);
        if (Double.parseDouble(xValue) - Double.parseDouble(yValue) > 0) {
          return 1;
        } else if (Double.parseDouble(xValue) - Double.parseDouble(yValue) < 0) {
          return -1;
        } else {
          return 0;
        }
      }
    };
    Collections.sort(instances, comparator);

		/*
     (3) Get each position that target value change,
			then calculate information gain of each position
		    find the maximum position value to be the threshold 		
		 */

    Distribution distribution = new Distribution(target, 2);
    distribution.add(1, instances);

    double parentEntropy = Entropy
        .calculateConti(target.getValues(), distribution.getFrequencyByTargetValue(),
            instances.size());

    int thresholdPos = 0;
    int currentShiftedIndex = 0;
    for (int i = 0; i < instances.size() - 1; i++) {
      HashMap<String, String> instancePair = instances.get(i).getAttributeValuePairs();
      String instanceValue = instancePair.get(attributeName);
      HashMap<String, String> instancePair2 = instances.get(i + 1).getAttributeValuePairs();
      String instanceValue2 = instancePair2.get(attributeName);

      if (!instanceValue.equals(instanceValue2)) {
        distribution.shift(1, 0, instances, currentShiftedIndex, i);
        currentShiftedIndex = i + 1;

        Hashtable<String, Double> leftBagFrequencyByTargetValue = distribution
            .getFrequencyByBagIndexByTargetValue(0);
        Hashtable<String, Double> rightBagFrequencyByTargetValue = distribution
            .getFrequencyByBagIndexByTargetValue(1);
        double currInfoGain = calculateConti(target, parentEntropy, leftBagFrequencyByTargetValue,
            rightBagFrequencyByTargetValue, instances.size(), i);

        if (currInfoGain - infoGain > 0) {
          infoGain = currInfoGain;
          thresholdPos = i;
        }
      }
    }

    // (4) Calculate threshold
    HashMap<String, String> a = instances.get(thresholdPos).getAttributeValuePairs();
    String aValue = a.get(attributeName);

    int thresholdPositionUpper =
        thresholdPos + 1 < instances.size() ? thresholdPos + 1 : thresholdPos;
    HashMap<String, String> b = instances.get(thresholdPositionUpper).getAttributeValuePairs();
    String bValue = b.get(attributeName);
    threshold = (Double.parseDouble(aValue) + Double.parseDouble(bValue)) / 2;

    // Initialize subset
    subset = new HashMap<String, ArrayList<Instance>>();
    ArrayList<Instance> left = new ArrayList<Instance>();
    ArrayList<Instance> right = new ArrayList<Instance>();
    for (int i = 0; i <= thresholdPos; i++) {
      left.add(instances.get(i));
    }
    for (int i = thresholdPos + 1; i < instances.size(); i++) {
      right.add(instances.get(i));
    }
    String leftName = "less" + threshold;
    String rightName = "more" + threshold;
    subset.put(leftName, left);
    subset.put(rightName, right);
  }

  private static double calculateConti(final Attribute target, final Double parentEntropy,
      final Hashtable<String, Double> leftDistribution,
      final Hashtable<String, Double> rightDistribution,
      final int instancesSize,
      final int index) {
    int subL = index + 1;
    int subR = instancesSize - index - 1;
    double subResL = ((double) subL) / ((double) instancesSize) *
        Entropy.calculateConti(target.getValues(), leftDistribution, subL);
    double subResR = ((double) subR) / ((double) instancesSize) *
        Entropy.calculateConti(target.getValues(), rightDistribution, subR);
    return parentEntropy - (subResL + subResR);
  }

  public Attribute getAttribute() {
    return attribute;
  }

  public double getThreshold() {
    return threshold;
  }

  public double getInfoGain() {
    return infoGain;
  }

  public HashMap<String, ArrayList<Instance>> getSubset() {
    return subset;
  }

  public String toString() {
    return "Attribute: " + attribute.getName() + "\n" + "Threshold: " + threshold + "\n"
        + "InfoGain: " + infoGain + "\n" + "Subset: " + subset;
  }
}