package C45CoreAlgorithm;

import DataDefination.Attribute;
import DataDefination.Instance;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

class Distribution {

  private Double totalFrequency;

  private Attribute targetAttribute;

  private final Hashtable<String, Double> frequencyByTargetValue;

  private final List<Hashtable<String, Double>> frequencyByBagIndexByTargetValue;

  Distribution(final Attribute targetAttribute, final int numberOfBag) {
    assert targetAttribute != null;
    assert numberOfBag > 0;

    this.totalFrequency = 0d;
    this.targetAttribute = targetAttribute;

    this.frequencyByTargetValue = new Hashtable<>();
    for (String targetAttributeValue : targetAttribute.getValues()) {
      this.frequencyByTargetValue.put(targetAttributeValue, 0d);
    }

    this.frequencyByBagIndexByTargetValue = new ArrayList<>(numberOfBag);
    for (int i = 0; i < numberOfBag; i++) {
      Hashtable<String, Double> clone = (Hashtable<String, Double>) this.frequencyByTargetValue
          .clone();
      this.frequencyByBagIndexByTargetValue.add(clone);
    }
  }

  void add(int bagIndex, List<Instance> instances) {
    assert bagIndex >= 0 && bagIndex < this.getNumberOfBag();
    assert instances != null;

    Hashtable<String, Double> currentBagFrequency = frequencyByBagIndexByTargetValue.get(bagIndex);
    String targetAttributeName = targetAttribute.getName();
    for (Instance instance : instances) {
      String instanceTargetValue = instance.getAttributeValuePairs().get(targetAttributeName);
      Double perBagClassFrequency = currentBagFrequency.get(instanceTargetValue) + 1;
      currentBagFrequency.put(instanceTargetValue, perBagClassFrequency);

      Double perClassFrequency = frequencyByTargetValue.get(instanceTargetValue) + 1;
      frequencyByTargetValue.put(instanceTargetValue, perClassFrequency);

      totalFrequency += 1;
    }
  }

  Double getTotalFrequency() {
    return totalFrequency;
  }

  Attribute getTargetAttribute() {
    return targetAttribute;
  }

  int getNumberOfBag() {
    return frequencyByBagIndexByTargetValue.size();
  }

  Hashtable<String, Double> getFrequencyByTargetValue() {
    return frequencyByTargetValue;
  }

  Hashtable<String, Double> getFrequencyByBagIndexByTargetValue(int bagIndex) {
    assert bagIndex >= 0 && bagIndex < this.getNumberOfBag();
    return frequencyByBagIndexByTargetValue.get(bagIndex);
  }
}
