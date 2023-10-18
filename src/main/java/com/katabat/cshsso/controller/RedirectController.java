package com.katabat.cshsso.controller;


import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import com.katabat.cshsso.service.PgpEncryptionService;


/**
 * DOCUMENT ME!
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
@Controller public class RedirectController {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Autowired private PgpEncryptionService pgpEncryptionService;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   response  DOCUMENT ME!
   *
   * @throws  IOException  DOCUMENT ME!
   * @throws  Exception    DOCUMENT ME!
   */
  @GetMapping("/redirect")
  public void redirectToExternal(HttpServletResponse response) throws Exception {
    String json = "{\n"
      + "\"DateOfBirth\":\"1986-01-01\",\n"
      + "\"FirstName\":\"Brett\",\n"
      + "\"OriginalAccountNumber\":\"5232229900002324\",\n"
      + "\"LastName\":\"Robinson\",\n"
      + "\"tag\":\"csh\",\n"
      + "\"timestamp\":1797522970000\n"
      + "}";

    String publicKeyString =
      "mQENBGUtRA0BCADH9cHv/3h4EwOWcAJ4NQIkdh+8lFpDenLPo5SfOLIQw8h/ozwV4cApg05zQkCwkfUwqQZTsFJc9Hwbpq51hgtUkghD2FcdpRAEdKPrw+cR9KzoX6C91KF+MqlfuEIbv6b0mmjc4T3Ivk1E1681IKM6ZSFYfACSzWpIQxKNQPuErz6ZONRVgBYMOHATfWUPXHuHPknhYwXPKVQjRCZopK89UPHmAMA+P2260GzENE/Us9kJMYG9aTd6ILCUAdil0HieYQ/cL5XvMi5M5EK6j2IepboStdz8xKB555glGwAhQIS2WwuQ3iItnXLpzswcMjNkdk1HA5j7QLCjWLoIXC5bABEBAAG0BmNzaHVhdIkBOAQTAQIAIgUCZS1EDQIbAwYLCQgHAwIGFQgCCQoLBBYCAwECHgECF4AACgkQp2Z2Kf6bYzDHAgf+MsNDn4YwarbQRtaGnSFDI2LFb6pcrFnadqKJ/+b9Q0bGMKun9ecj3sJAupiJLW4bO9mVjcKFcicR3qkLOX7bK7+asd6+BJfbMUM6PVaxursmNwvLD6n+2qrJsF9jL0SisbS1Lb40QWHFvQPy8LbnJHXZNNC5zDRfwjAn+etsRSWqmvkFhIH62gLYDRQrqeRmplZbnSZpcaoiFvnjBmuefk+yx7uluCQh6ruLubBSqqrIZti2Jn1RqORfdzhuffdhU9VLc0WvzPkKDM14YDRb9y9wyVx4hVX7SNk9Mg/5ZGpoyngXkWFMTr4zwp154Pfvh7usMgPbBm1DCyIWjhhHjrkBDQRlLUQNAQgA0PAQrnMYqXYpsD8gtjeL08T93gh9434Aalde9GeKhnvv+DiHqNpQuLK6S74Wj8hs0emd2vZAVrGYYk9KThfLFGFALJAPouFqOi9JTbOKV0YXcGlgYjWF2eDFxYrk7yEiL25FvS9Hx775xssE6mdYM766ZtXckVTcY5vwD08uP93fXXjO/b2/go3U2cGJMxIi4uFFmlZaT0ZHuQDH51jSogfyDAecvnkrVM0IPcGc6/LF4QHwVl9Si9gKzUMy+XsvEkr0MuRABaWEbNIi+edW1mAEUV7vo7HoHeYSokmkm9a/sGu2GvTvcIDK1KBB2SqLGSgfrLEJVGjRJfuiPjeCZwARAQABiQEfBBgBAgAJBQJlLUQNAhsMAAoJEKdmdin+m2MweeEIALsDUYnD4dmCRCb5SgOE+CE5ZEyIwz97vRSoez4HDoQNkZVlibbEx3JdWoh9DDHrCNDGnsjnRof7wX9ZB68U+wwi8xXAss6/Vl2afJYgJ9OEGelwbaT9ThU66ciRVpQuhbtr6PYQNrp5CF87axtiZ6+o+s1ePF8llmqcbwZJjU+1IA4dg8bEfBk07doaGxBr5SbcgytprMrmZwnLtV11cfmJmNKcOt2U9pb5TTJiMpjFzDxm2Mq8b2ihA1E9CkRlk/FYZMNP5eQRrOWAc0Dv0oEi6cR05yo4KC1ggaJRgizr659n4uPMro8sI1ZBOT65oAdmDJlL+2klkzO2R9Pwrk0=";
    String encryptedToken  = pgpEncryptionService.encryptAndBase64Encode(json, publicKeyString);

    String externalUrl = "https://cshuat.katabat.com/flexSiteSso?portfolioId=352001&token=" + encryptedToken;
    response.sendRedirect(externalUrl);
  }
} // end class RedirectController
