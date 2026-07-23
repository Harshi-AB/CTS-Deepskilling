import '../Stylesheets/mystyle.css'

// Converts a raw decimal (e.g. 0.9296) into a percentage string (e.g. "92.96%")
const percentToDecimal = (decimal) => {
  return (decimal.toFixed(2) + '%')
}

// Calculates the score as a percentage of total marks obtained out of the goal
const calcScore = (total, goal) => {
  return percentToDecimal(total / goal)
}

// Functional component that displays a student's details and their calculated score
export const CalculateScore = ({ Name, School, total, goal }) => (
  <div className="formatstyle">
    <h1><font color="Brown">Student Details:</font></h1>
    <div className="Name">
      <b> <span> Name:  </span> </b>
      <span>{Name}</span>
    </div>
    <div className="School">
      <b> <span> School: </span> </b>
      <span>{School}</span>
    </div>
    <div className="Total">
      <b><span>Total:</span> </b>
      <span>{total}</span>
      <span>Marks</span>
    </div>
    <div className="Score">
      <b>Score:</b>
      <span>
        {calcScore(
          total,
          goal
        )}
      </span>
    </div>
  </div>
)
