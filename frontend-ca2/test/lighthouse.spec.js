const lighthouse = require('lighthouse');
const chromeLauncher = require('chrome-launcher');
const assert = require('assert');
const fs = require('fs');
const path = require('path');

// Create reports directory if it doesn't exist
const reportsDir = path.join(__dirname, '../reports');
if (!fs.existsSync(reportsDir)) {
  fs.mkdirSync(reportsDir);
}

async function launchChromeAndRunLighthouse(url, opts = {}, config = null) {
  const chrome = await chromeLauncher.launch({chromeFlags: ['--headless']});
  opts.port = chrome.port;

  const results = await lighthouse(url, opts, config);
  await chrome.kill();

  return results;
}

function generateReport(results, category) {
  const score = results.lhr.categories[category].score * 100;
  const audits = results.lhr.audits;
  const failedAudits = Object.values(audits)
    .filter(audit => audit.score !== null && audit.score < 1)
    .map(audit => ({
      id: audit.id,
      title: audit.title,
      score: audit.score * 100,
      description: audit.description
    }));

  return {
    score,
    failedAudits,
    details: results.lhr.categories[category]
  };
}

function saveHTMLReport(results, category) {
  const report = results.report;
  const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
  const filename = `lighthouse-${category}-${timestamp}.html`;
  const filepath = path.join(reportsDir, filename);
  
  fs.writeFileSync(filepath, report);
  return filepath;
}

describe('Lighthouse Tests', () => {
  const url = 'http://localhost:3000';
  const thresholds = {
    performance: 80,
    accessibility: 80,
    'best-practices': 80,
    seo: 80
  };

  it('should meet performance thresholds', async function() {
    this.timeout(60000);

    const results = await launchChromeAndRunLighthouse(url);
    const report = generateReport(results, 'performance');
    const htmlReportPath = saveHTMLReport(results, 'performance');

    console.log('\nPerformance Report:');
    console.log(`Score: ${report.score.toFixed(1)}%`);
    console.log(`HTML Report saved to: ${htmlReportPath}`);
    
    if (report.failedAudits.length > 0) {
      console.log('\nFailed Audits:');
      report.failedAudits.forEach(audit => {
        console.log(`- ${audit.title} (Score: ${audit.score.toFixed(1)}%)`);
        console.log(`  Description: ${audit.description}`);
      });
    }

    assert.ok(report.score >= thresholds.performance,
      `Performance score ${report.score.toFixed(1)}% is below threshold ${thresholds.performance}%`);
  });

  it('should meet accessibility thresholds', async function() {
    this.timeout(60000);

    const results = await launchChromeAndRunLighthouse(url);
    const report = generateReport(results, 'accessibility');
    const htmlReportPath = saveHTMLReport(results, 'accessibility');

    console.log('\nAccessibility Report:');
    console.log(`Score: ${report.score.toFixed(1)}%`);
    console.log(`HTML Report saved to: ${htmlReportPath}`);
    
    if (report.failedAudits.length > 0) {
      console.log('\nFailed Audits:');
      report.failedAudits.forEach(audit => {
        console.log(`- ${audit.title} (Score: ${audit.score.toFixed(1)}%)`);
        console.log(`  Description: ${audit.description}`);
      });
    }

    assert.ok(report.score >= thresholds.accessibility,
      `Accessibility score ${report.score.toFixed(1)}% is below threshold ${thresholds.accessibility}%`);
  });

  it('should meet best practices thresholds', async function() {
    this.timeout(60000);

    const results = await launchChromeAndRunLighthouse(url);
    const report = generateReport(results, 'best-practices');
    const htmlReportPath = saveHTMLReport(results, 'best-practices');

    console.log('\nBest Practices Report:');
    console.log(`Score: ${report.score.toFixed(1)}%`);
    console.log(`HTML Report saved to: ${htmlReportPath}`);
    
    if (report.failedAudits.length > 0) {
      console.log('\nFailed Audits:');
      report.failedAudits.forEach(audit => {
        console.log(`- ${audit.title} (Score: ${audit.score.toFixed(1)}%)`);
        console.log(`  Description: ${audit.description}`);
      });
    }

    assert.ok(report.score >= thresholds['best-practices'],
      `Best practices score ${report.score.toFixed(1)}% is below threshold ${thresholds['best-practices']}%`);
  });

  it('should meet SEO thresholds', async function() {
    this.timeout(60000);

    const results = await launchChromeAndRunLighthouse(url);
    const report = generateReport(results, 'seo');
    const htmlReportPath = saveHTMLReport(results, 'seo');

    console.log('\nSEO Report:');
    console.log(`Score: ${report.score.toFixed(1)}%`);
    console.log(`HTML Report saved to: ${htmlReportPath}`);
    
    if (report.failedAudits.length > 0) {
      console.log('\nFailed Audits:');
      report.failedAudits.forEach(audit => {
        console.log(`- ${audit.title} (Score: ${audit.score.toFixed(1)}%)`);
        console.log(`  Description: ${audit.description}`);
      });
    }

    assert.ok(report.score >= thresholds.seo,
      `SEO score ${report.score.toFixed(1)}% is below threshold ${thresholds.seo}%`);
  });
}); 