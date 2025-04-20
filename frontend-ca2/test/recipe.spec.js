const sinon = require('sinon');
const assert = require('assert');
const fs = require('fs');
const http = require('http');
const config = require('../config');

describe('Recipe Frontend Tests', () => {
  let sandbox;
  let app;
  let server;
  let createServer;

  before(() => {
    // Stub fs.readFileSync globally before requiring the server
    sandbox = sinon.createSandbox();
    sandbox.stub(fs, 'readFileSync').returns('mock-css-content');
    createServer = require('../fe-server');
  });

  after(() => {
    sandbox.restore();
  });

  beforeEach(() => {
    // Create a new sandbox for each test
    sandbox = sinon.createSandbox();
    server = createServer();
    app = server._events.request;
  });

  afterEach(() => {
    sandbox.restore();
    if (server && server.listening) {
      server.close();
    }
  });

  describe('Server Setup', () => {
    it('should load configuration correctly', () => {
      assert.ok(config.development.port);
      assert.ok(config.development.webservice_host);
    });

    it('should create server with correct port', () => {
      assert.ok(server instanceof http.Server);
    });
  });

  describe('Recipe Form Submission', () => {
    it('should handle form submission correctly', (done) => {
      const mockReq = {
        method: 'POST',
        url: '/',
        on: function(event, callback) {
          if (event === 'data') callback('name=Test+Recipe&ingredients=ingredient1,ingredient2&prepTimeInMinutes=30');
          if (event === 'end') callback();
        }
      };

      const mockRes = {
        writeHead: sandbox.spy(),
        write: sandbox.spy(),
        end: sandbox.spy()
      };

      const httpStub = sandbox.stub(http, 'request').returns({
        on: sandbox.stub(),
        write: sandbox.stub(),
        end: sandbox.stub(),
        setHeader: sandbox.stub()
      });

      app(mockReq, mockRes);

      setTimeout(() => {
        assert.ok(mockRes.writeHead.calledWith(200, {'Content-Type': 'text/html'}));
        assert.ok(mockRes.write.calledWith(sinon.match('New recipe saved successfully!')));
        done();
      }, 100);
    });
  });

  describe('Recipe List Display', () => {
    it('should display recipes correctly', (done) => {
      const mockReq = {
        method: 'GET',
        url: '/',
      };

      const mockRes = {
        writeHead: sandbox.spy(),
        write: sandbox.spy(),
        end: sandbox.spy()
      };

      const testRecipe = {
        name: 'Test Recipe',
        ingredients: ['ingredient1', 'ingredient2'],
        prepTimeInMinutes: 30
      };

      const mockResponse = {
        on: function(event, callback) {
          if (event === 'data') callback(JSON.stringify([testRecipe]));
          if (event === 'end') callback();
        }
      };

      const httpStub = sandbox.stub(http, 'request')
        .yields(mockResponse)
        .returns({
          on: sandbox.stub(),
          write: sandbox.stub(),
          end: sandbox.stub()
        });

      app(mockReq, mockRes);

      setTimeout(() => {
        try {
          const writeArgs = mockRes.write.args.map(args => args[0]).join('');
          assert.ok(writeArgs.includes('Your Previous Recipes'));
          assert.ok(writeArgs.includes('Test Recipe'));
          assert.ok(writeArgs.includes('30'));
          done();
        } catch (error) {
          done(error);
        }
      }, 2500);
    });
  });

  describe('Error Handling', () => {
    it('should handle server errors gracefully', (done) => {
      const mockReq = {
        method: 'GET',
        url: '/',
      };

      const mockRes = {
        writeHead: sandbox.spy(),
        write: sandbox.spy(),
        end: sandbox.spy()
      };

      const httpStub = sandbox.stub(http, 'request')
        .returns({
          on: (event, callback) => {
            if (event === 'error') callback(new Error('Test error'));
          },
          end: () => {}
        });

      app(mockReq, mockRes);

      setTimeout(() => {
        assert.ok(mockRes.write.calledWith(sinon.match('Error')));
        done();
      }, 100);
    });
  });
}); 