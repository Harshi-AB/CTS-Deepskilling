import React from 'react';

// Import the CSS Module. Webpack/CRA rewrites class names uniquely,
// so we reference them through the "styles" object (e.g. styles.box)
// instead of plain string class names.
import styles from './CohortDetails.module.css';

/**
 * CohortDetails
 * Renders a single cohort's information inside a styled "box".
 * The <h3> heading color reflects the cohort's status:
 *   - "ongoing"  -> green
 *   - anything else (e.g. "completed") -> blue
 *
 * Expected prop shape:
 * cohort = {
 *   name: string,
 *   status: string,        // e.g. "ongoing" | "completed"
 *   startDate: string,
 *   endDate: string,
 *   description: string
 * }
 */
function CohortDetails({ cohort }) {
  const headingStyle = {
    color: cohort.status === 'ongoing' ? 'green' : 'blue',
  };

  return (
    // "box" class from the CSS Module applied to the container div
    <div className={styles.box}>
      <h3 style={headingStyle}>{cohort.name}</h3>

      <dl>
        <dt>Status</dt>
        <dd>{cohort.status}</dd>

        <dt>Start Date</dt>
        <dd>{cohort.startDate}</dd>

        <dt>End Date</dt>
        <dd>{cohort.endDate}</dd>

        <dt>Description</dt>
        <dd>{cohort.description}</dd>
      </dl>
    </div>
  );
}

export default CohortDetails;
