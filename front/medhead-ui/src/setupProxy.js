const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    "/api/v1/specialities",
    createProxyMiddleware({
      target: "https://speciality-service:9443",
      changeOrigin: true,
      secure: false,
    })
  );

  app.use(
    "/api/v1/destination",
    createProxyMiddleware({
      target: "https://destination-service:8443",
      changeOrigin: true,
      secure: false,
    })
  );
};
