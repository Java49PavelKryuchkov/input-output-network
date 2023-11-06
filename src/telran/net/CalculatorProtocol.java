package telran.net;

import java.net.Authenticator.RequestorType;

public class CalculatorProtocol implements ApplProtocol {

	@Override
	public Response getResponse(Request request) {
		Response response = null;
		try{
			String operator = request.requestType();
		Double[] operands = (Double[]) request.requestData();
			double res = switch(operator) {
			case "Add" -> operands[0] + operands[1];
			case "Subtract" -> operands[0] - operands[1];
			case "Divide" -> operands[0] * operands[1];
			case "Multiply" -> operands[0] / operands[1];
			default -> Double.NaN;
		};
		response = Double.isNaN(res) ?
				new Response(ResponseCode.WRONG_TYPE, operator + " is wrong request type"):
					new Response(ResponseCode.OK, res);
		return response;
		}catch (Exception e) {
			return new Response(ResponseCode.WRONG_DATA, e.getMessage());
		}
	
}
}
