/**
 * Copyright (c) Microsoft Corporation
 * <p/>
 * All rights reserved.
 * <p/>
 * MIT License
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.microsoft.tooling.msservices.model.vm;

import com.microsoft.tooling.msservices.helpers.NotNull;
import com.microsoft.tooling.msservices.model.ServiceTreeItem;


public class Endpoint implements ServiceTreeItem {
  private boolean loading;
  private String name;
  private String protocol;
  private int privatePort;
  private int publicPort;

  public Endpoint(@NotNull String name, @NotNull String protocol, int privatePort, int publicPort) {
    this.name = name;
    this.protocol = protocol;
    this.privatePort = privatePort;
    this.publicPort = publicPort;
  }

  @Override
  public boolean isLoading() {
    return loading;
  }

  @Override
  public void setLoading(boolean loading) {
    this.loading = loading;
  }

  @NotNull
  public String getName() {
    return name;
  }

  @NotNull
  public String getProtocol() {
    return protocol;
  }

  public int getPrivatePort() {
    return privatePort;
  }

  public int getPublicPort() {
    return publicPort;
  }

  @Override
  public String toString() {
    return name + (loading ? " (loading...)" : "");
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public void setPrivatePort(int privatePort) {
    this.privatePort = privatePort;
  }

  public void setPublicPort(int publicPort) {
    this.publicPort = publicPort;
  }
}