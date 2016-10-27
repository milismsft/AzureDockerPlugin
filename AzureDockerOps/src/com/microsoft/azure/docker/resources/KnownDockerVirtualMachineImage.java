package com.microsoft.azure.docker.resources;

import com.microsoft.azure.management.compute.ImageReference;
import com.microsoft.azure.management.compute.KnownLinuxVirtualMachineImage;

public enum KnownDockerVirtualMachineImage {
  /** Ubuntu_Snappy_Core 15.04 */
  UBUNTU_SNAPPY_CORE_15_04("Canonical", "Ubuntu_Snappy_Core", "15.04", "latest"),
  /** CoreOS CoreOS Stable */
  COREOS_STABLE_LATEST("CoreOS", "CoreOS", "Stable", "latest" /* 899.17.0 */),
  /** OpenLogic CentOS 7.2 */
  OPENLOGIC_CENTOS_7_2("OpenLogic", "CentOS", "7.2", "latest" /* 7.2.20160620 */),
  /** UbuntuServer 14.04LTS. */
  UBUNTU_SERVER_14_04_LTS("Canonical", "UbuntuServer", "14.04.4-LTS", "latest"),
  /** UbuntuServer 16.04LTS. */
  UBUNTU_SERVER_16_04_LTS("Canonical", "UbuntuServer", "16.04.0-LTS", "latest");

  private final String publisher;
  private final String offer;
  private final String sku;
  private final String version;

  KnownDockerVirtualMachineImage(String publisher, String offer, String sku, String version) {
    this.publisher = publisher;
    this.offer = offer;
    this.sku = sku;
    this.version = version;
  }

  /**
   * @return the name of the image publisher
   */
  public String publisher() {
    return this.publisher;
  }

  /**
   * @return the name of the image offer
   */
  public String offer() {
    return this.offer;
  }

  /**
   * @return the name of the image SKU
   */
  public String sku() {
    return this.sku;
  }

  /**
   * @return the name of the image SKU
   */
  public String version() {
    return this.version;
  }

  /**
   * @return the image reference
   */
  public ImageReference imageReference() {
    return new ImageReference()
        .withPublisher(publisher())
        .withOffer(offer())
        .withSku(sku())
        .withVersion(version);
  }
}
