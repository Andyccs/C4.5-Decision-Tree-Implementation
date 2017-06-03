package C45CoreAlgorithm;

import DataDefination.Attribute;
import DataDefination.Instance;
import java.util.Hashtable;
import java.util.List;

class Distribution {

  private final Hashtable<String, Double> frequencyByTargetValue;
  private final Hashtable<String, Double>[] frequencyByBagIndexByTargetValue;
  private Double totalFrequency;
  private Attribute targetAttribute;

  Distribution(final Attribute targetAttribute, final int numberOfBag) {
    assert targetAttribute != null;
    assert numberOfBag > 0;

    this.totalFrequency = 0d;
    this.targetAttribute = targetAttribute;

    this.frequencyByTargetValue = new Hashtable<>();
    for (String targetAttributeValue : targetAttribute.getValues()) {
      this.frequencyByTargetValue.put(targetAttributeValue, 0d);
    }

    this.frequencyByBagIndexByTargetValue = new Hashtable[numberOfBag];
    for (int i = 0; i < numberOfBag; i++) {
      this.frequencyByBagIndexByTargetValue[i] =
          (Hashtable<String, Double>) this.frequencyByTargetValue.clone();
    }
  }

  void add(int bagIndex, List<Instance> instances) {
    assert bagIndex >= 0 && bagIndex < this.getNumberOfBag();
    assert instances != null;

    Hashtable<String, Double> currentBagFrequency = frequencyByBagIndexByTargetValue[bagIndex];
    String targetAttributeName = targetAttribute.getName();
    for (Instance instance : instances) {
      String instanceTargetValue = instance.getAttributeValuePairs().get(targetAttributeName);
      Double perBagClassFrequency = currentBagFrequency.get(instanceTargetValue) + 1;
      currentBagFrequency.put(instanceTargetValue, perBagClassFrequency);

      Double perClassFrequency = frequencyByTargetValue.get(instanceTargetValue) + 1;
      assert perClassFrequency >= 0;
      frequencyByTargetValue.put(instanceTargetValue, perClassFrequency);

      totalFrequency += 1;
    }
  }

  void shift(int fromBagIndex, int toBagIndex, List<Instance> instances,
      int fromInclusiveInstancesIndex, int toInclusiveInstancesIndex) {
    assert fromBagIndex >= 0 && fromBagIndex < this.getNumberOfBag();
    assert toBagIndex >= 0 && toBagIndex < this.getNumberOfBag();
    assert fromBagIndex != toBagIndex;
    assert fromInclusiveInstancesIndex >= 0 && fromInclusiveInstancesIndex < instances.size();
    assert toInclusiveInstancesIndex >= 0 && toInclusiveInstancesIndex < instances.size();
    assert fromInclusiveInstancesIndex <= toInclusiveInstancesIndex;
    assert instances != null;

    Hashtable<String, Double> fromBag = frequencyByBagIndexByTargetValue[fromBagIndex];
    Hashtable<String, Double> toBag = frequencyByBagIndexByTargetValue[toBagIndex];

    String targetAttributeName = targetAttribute.getName();
    for (int i = fromInclusiveInstancesIndex; i <= toInclusiveInstancesIndex; i++) {
      Instance instance = instances.get(i);
      String instanceTargetValue = instance.getAttributeValuePairs().get(targetAttributeName);

      Double fromBagClassFrequency = fromBag.get(instanceTargetValue) - 1;
      assert fromBagClassFrequency >= 0;
      fromBag.put(instanceTargetValue, fromBagClassFrequency);

      Double toBagClassFrequency = toBag.get(instanceTargetValue) + 1;
      assert toBagClassFrequency >= 0;
      toBag.put(instanceTargetValue, toBagClassFrequency);
    }
  }

  Double getTotalFrequency() {
    return totalFrequency;
  }

  Attribute getTargetAttribute() {
    return targetAttribute;
  }

  int getNumberOfBag() {
    return frequencyByBagIndexByTargetValue.length;
  }

  Hashtable<String, Double> getFrequencyByTargetValue() {
    return frequencyByTargetValue;
  }

  Hashtable<String, Double> getFrequencyByBagIndexByTargetValue(int bagIndex) {
    assert bagIndex >= 0 && bagIndex < this.getNumberOfBag();
    return frequencyByBagIndexByTargetValue[bagIndex];
  }
}
